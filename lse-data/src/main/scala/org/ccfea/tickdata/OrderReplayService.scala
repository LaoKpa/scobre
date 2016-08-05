package org.ccfea.tickdata

import java.util.Date

import collection.JavaConversions._
import collection.JavaConverters._
import scala.collection.parallel
import grizzled.slf4j.Logger
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.transport.TServerSocket
import org.ccfea.tickdata.order.{LimitOrder, OrderWithVolume}
import org.ccfea.tickdata.order.offset.{MidPriceOffsetOrder, Offsetting, OppositeSideOffsetOrder, SameSideOffsetOrder}
import org.ccfea.tickdata.storage.shuffled.copier
import org.ccfea.tickdata.collector.MultivariateTimeSeriesCollector
import org.ccfea.tickdata.conf.{BuildInfo, ServerConf}
import org.ccfea.tickdata.event.{OrderEvent, TickDataEvent}
import org.ccfea.tickdata.simulator.{ClearingMarketState, MarketState, OrderReplayer, Quote}
import org.ccfea.tickdata.storage.csv.MultivariateCsvDataCollator
import org.ccfea.tickdata.storage.hbase.HBaseRetriever
import org.ccfea.tickdata.storage.shuffled._
import org.ccfea.tickdata.storage.shuffled.copier.{OrderSignCopier, PriceCopier, TickCopier, VolumeCopier}
import org.ccfea.tickdata.storage.thrift.MultivariateThriftCollator
import org.ccfea.tickdata.thrift.OrderReplay

import collection.JavaConversions._
import scala.collection.immutable.ListMap

/**
 * A server that provides order-replay simulation results over the network.
 * It uses Apache Thrift so that clients can easily be written in other languages.
 *
 * (C) Steve Phelps 2014
 */
object OrderReplayService extends ReplayApplication {

  class ThriftReplayer(val eventSource: Iterable[TickDataEvent],
                 val dataCollectors: Map[String, MarketState => Option[AnyVal]],
                  val marketState: MarketState)
    extends MultivariateTimeSeriesCollector with MultivariateThriftCollator

  class CsvReplayer(val eventSource: Iterable[TickDataEvent],
                 val dataCollectors: Map[String, MarketState => Option[AnyVal]],
                  val marketState: MarketState, val outFileName: Option[String])
    extends MultivariateTimeSeriesCollector with MultivariateCsvDataCollator

  var tickCache = Map[(String, Offsetting.Value, Option[(Long, Long)]), Seq[TickDataEvent]]()

  val logger = Logger("org.ccmainfea.tickdata.OrderReplayService")


  def getOffsettedTicks(ticks: Seq[TickDataEvent], offsetting: Offsetting.Value)(implicit conf: ServerConf) = {
    val marketState = newMarketState
    offsetting match {
      case Offsetting.NoOffsetting =>
        ticks
      case Offsetting.MidPrice =>
        new OffsettedTicks(marketState, ticks,
                            (limitOrder: LimitOrder, quote: Quote) => new MidPriceOffsetOrder(limitOrder, quote))
      case Offsetting.SameSide =>
        new OffsettedTicks(marketState, ticks,
                            (limitOrder: LimitOrder, quote: Quote) => new SameSideOffsetOrder(limitOrder, quote))
      case Offsetting.OppositeSide =>
        new OffsettedTicks(marketState, ticks,
                              (limitOrder: LimitOrder, quote: Quote) => new OppositeSideOffsetOrder(limitOrder, quote))
    }
  }

  def getShuffledData(assetId: String,
                          proportionShuffling: Double,
                          windowSize: Int, intraWindow: Boolean,
                          offsetting: Offsetting.Value, shuffledAttribute: ShuffledAttribute.Value,
                          dateRange: Option[(Long, Long)])(implicit conf: ServerConf): Seq[TickDataEvent] = {
    val ticks = if (tickCache.contains((assetId, offsetting))) {
      tickCache((assetId, offsetting, dateRange))
    } else {
      val start = dateRange match {
        case None => None
        case Some((t0, t1)) => Some(new Date(t0))
      }
      val end = dateRange match {
        case None => None
        case Some((t0, t1)) => Some(new Date(t1))
      }
      val originalData = new HBaseRetriever(selectedAsset = assetId, startDate = start, endDate = end).toList
      val offsettedTicks = getOffsettedTicks(originalData, offsetting).toList
      tickCache += ((assetId, offsetting, dateRange) -> offsettedTicks)
      offsettedTicks
    }

    val swapper = shuffledAttribute match {
      case ShuffledAttribute.AllAttributes => new TickCopier()
      case ShuffledAttribute.Volume => new VolumeCopier()
      case ShuffledAttribute.OrderSign => new OrderSignCopier()
      case ShuffledAttribute.Price => new PriceCopier()
    }

    if (intraWindow)
      new IntraWindowRandomPermutation(ticks, proportionShuffling, windowSize, swapper)
    else
      new RandomPermutation(ticks, proportionShuffling, windowSize, swapper)

  }

  def executeShuffledReplay(assetId: String, variables: List[String],
                                      proportionShuffling: Double, windowSize: Int, intraWindow: Boolean,
                                      offsetting: Int, attribute: Int,
                                      dateRange: Option[(Long, Long)])(implicit conf: ServerConf):
                                                java.util.Map[String, java.util.List[java.lang.Double]] = {
    logger.info("Shuffled replay for " + assetId + " with windowSize " + windowSize +
                      ", offsetting " + offsetting + " and percentage " + proportionShuffling)
    dateRange match {
      case Some((t0, t1)) =>
        logger.info("between " + new Date(t0) + " and " + new Date(t1))
      case None =>
    }
    val marketState = newMarketState(conf)
    val shuffledTicks =
      getShuffledData(assetId, proportionShuffling, windowSize, intraWindow,
                      Offsetting(offsetting), ShuffledAttribute(attribute), dateRange)
    val replayer =
      new ThriftReplayer(shuffledTicks, dataCollectors(variables), marketState)
    runSimulation(replayer)
    replayer.result
  }

  def getTicks(assetId: String, variables: java.util.List[String],
               startDateTime: Long,
               endDateTime: Long): HBaseRetriever = {
    val startDate = new Date(startDateTime)
    val endDate = new Date(endDateTime)
    logger.info("Using data for " + assetId + " between " + startDate + " and " + endDate)
    new HBaseRetriever(selectedAsset = assetId, startDate = Some(startDate), endDate = Some(endDate))
  }

  def runSimulation(replayer: MultivariateTimeSeriesCollector) = {
    logger.info("Starting simulation... ")
    replayer.run()
    logger.info("done.")
  }

  def main(args: Array[String]): Unit = {

    implicit val conf = new ServerConf(args)
    val port: Int = conf.port()

    val processor = new org.ccfea.tickdata.thrift.OrderReplay.Processor(new OrderReplay.Iface {

      override def replay(assetId: String, variables: java.util.List[String],
                            startDateTime: Long,
                            endDateTime: Long): java.util.Map[String, java.util.List[java.lang.Double]] = {
        val marketState = newMarketState(conf)
        val ticks = getTicks(assetId, variables, startDateTime, endDateTime)
        val replayer = new ThriftReplayer(ticks, dataCollectors(List() ++ variables), marketState)
        runSimulation(replayer)
        replayer.result
      }

      override def replayToCsv(assetId: String, variables: java.util.List[String],
                                startDateTime: Long, endDateTime: Long,
                                csvFileName: String): Long = {
        val marketState = newMarketState(conf)
        val ticks = getTicks(assetId, variables, startDateTime, endDateTime)
        val replayer =
          new CsvReplayer(ticks, dataCollectors(List() ++ variables), marketState, Some(csvFileName))
        runSimulation(replayer)
        return 0
      }

      override def shuffledReplayDateRange(assetId: String, variables: java.util.List[String],
                                    proportionShuffling: Double, windowSize: Int, intraWindow: Boolean,
                                      offsetting: Int, attribute: Int, startDateTime: Long, endDateTime: Long):
                                                java.util.Map[String, java.util.List[java.lang.Double]] = {
        executeShuffledReplay(assetId, List() ++ variables, proportionShuffling, windowSize,
                                intraWindow, offsetting, attribute, Some((startDateTime, endDateTime)))
      }

      override def shuffledReplay(assetId: String, variables: java.util.List[String], proportionShuffling: Double,
                                    windowSize: Int, intraWindow: Boolean,
                                    offsetting: Int,
                                  attribute: Int): java.util.Map[String, java.util.List[java.lang.Double]] = {
        executeShuffledReplay(assetId, List() ++ variables, proportionShuffling, windowSize, intraWindow,
                                    offsetting, attribute, None)
      }

    })

    val serverTransport = new TServerSocket(port)
    val server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor))

    logger.info("SCOBRE order-replay server version " + BuildInfo.version)
    logger.info("Server running on port " + port + "... ")
    server.serve()
    logger.info("Server terminated.")
  }

}

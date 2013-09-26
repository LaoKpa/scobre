package org.ccfea.tickdata

import scala.slick.driver.MySQLDriver.simple._
import RelationalTables._
import Database.threadLocalSession

import java.text.SimpleDateFormat

import net.sourceforge.jasa.market._
import net.sourceforge.jasa.agent.SimpleTradingAgent
import net.sourceforge.jasa.market.auctioneer.ContinuousDoubleAuctioneer

import net.sourceforge.jabm.SimulationTime

import javax.swing.{JLabel, JFrame, SwingUtilities}

import collection.JavaConversions._
import java.awt.BorderLayout

//import scala.tools.nsc._

class MarketState {

  val book = new FourHeapOrderBook()
  var lastChanged: Option[SimulationTime] = None

  val orderMap = collection.mutable.Map[String, Order]()

  def processEvent(ev: Event) = {

    lastChanged = Some(new SimulationTime(ev.timeStamp))

    ev match {

          case Event(id, EventType.OrderSubmitted,
                      messageSequenceNumber, timeStamp,
                tiCode, marketSegmentCode,
                Some(marketMechanismType), Some(aggregateSize), Some(buySellInd),
                Some(orderCode),
                None, Some(broadcastUpdateAction), Some(marketSectorCode), Some(marketMechanismGroup), Some(price), Some(singleFillInd),
                None, None, None, None, None)

          => {

                  val order = new Order()
                  order.setPrice(price.toDouble)
                  order.setQuantity(aggregateSize.toInt)
                  order.setAgent(new SimpleTradingAgent())
                  order.setIsBid(buySellInd equals "B")
                  order.setTimeStamp(lastChanged.get)
                  if (orderMap.contains(orderCode)) {
                    println("Order revision to " + orderMap(orderCode))
                  }
                  orderMap(orderCode) = order
                  if (marketMechanismType equals "LO") {
                    processLimitOrder(order)
                  } else {
                    processMarketOrder(order)
                  }
          }

          case Event(id, EventType.OrderDeleted | EventType.OrderExpired | EventType.TransactionLimit,
                      messageSequenceNumber, timeStamp,
                      tiCode, marketSegmentCode,
                      marketMechanismType, aggregateSize, buySellInd,
                      Some(orderCode),
                      tradeSize, broadcastUpdateAction, marketSectorCode, marketMechanismGroup, price, singleFillInd,
                      None, None, None, None, None)

          => {
                  println("Removing order " + orderCode)
                  if (orderMap.contains(orderCode)) {
                    val removedOrder = orderMap(orderCode)
                    println("Removed " + removedOrder)
                    book.remove(removedOrder)
                  } else {
                    println("Unable to find order " + orderCode)
                  }

          }

          case Event(id, EventType.OrderFilled,
                  messageSequenceNumber, timeStamp,
                  tiCode, marketSegmentCode,
                  marketMechanismType, aggregateSize, buySellInd,
                  Some(orderCode),
                  tradeSize, broadcastUpdateAction, marketSectorCode, marketMechanismGroup, price, singleFillInd,
                  matchingOrderCode, resultingTradeCode, None, None, None)

          => {
            println("Order matched " + orderCode)
            if (orderMap.contains(orderCode)) {
              val order = orderMap(orderCode)
              println("Found order " + order)
              book.remove(order)
            } else {
              println("Unable to find order " + orderCode)
            }

          }
          case _ => // println("Do not know how to process " + ev)
    }
  }

  def processLimitOrder(order: Order) = {
    if (order.isAsk) {
      book.insertUnmatchedAsk(order)
    } else {
      book.insertUnmatchedBid(order)
    }
//    book.matchOrders()
  }

  def processMarketOrder(order: Order) = {
    val otherSide =
      if (order.isBid) book.getUnmatchedAsks
      else book.getUnmatchedBids
    var qty = order.getQuantity
    for (potentialMatch <- otherSide if qty >= potentialMatch.getQuantity) {
      val limitOrderQty = potentialMatch.getQuantity
      book.remove(potentialMatch)
      qty = qty - limitOrderQty
    }
  }

  def printState = {
    book.printState()
  }

  def midPrice: Option[Double] = {
    val bid = book.getHighestUnmatchedBid
    val ask = book.getLowestUnmatchedAsk
    if (bid == null || ask == null) {
      None
    } else {
      Some((bid.getPrice + ask.getPrice) / 2.0)
    }
  }

}

class MarketStateWithGUI extends MarketState {

  val view: OrderBookView = new OrderBookView(this)

  override def processEvent(ev: Event) = {
    val result = super.processEvent(ev)
    println(ev)
    view.update
    result
  }
}

class OrderBookView(val market: MarketState) {
  val df = new SimpleDateFormat("hh:mm:ss dd/MM yyyy")
  val auctioneer = new ContinuousDoubleAuctioneer()
  auctioneer.setOrderBook(market.book)
  val orderBookView = new net.sourceforge.jasa.view.OrderBookView()
  orderBookView.setAuctioneer(auctioneer)
  orderBookView.setMaxDepth(30)
  orderBookView.afterPropertiesSet()
  val myFrame = new JFrame()
  val timeLabel = new JLabel()
  myFrame.setLayout(new BorderLayout())
  myFrame.add(orderBookView, BorderLayout.CENTER)
  myFrame.add(timeLabel, BorderLayout.NORTH)
  myFrame.pack()
  myFrame.setVisible(true)

  def update = {
    SwingUtilities.invokeAndWait(new Runnable() {
      def run() = {
        orderBookView.update();
        orderBookView.notifyTableChanged()
        timeLabel.setText(df.format(new java.util.Date(market.lastChanged.get.getTicks)))
      }
    })
  }
}

class OrderFlow(val orders: Seq[Event], val market: MarketState = new MarketStateWithGUI()) {

  def map[B](f: MarketState => B): Seq[B] = {
    orders.map(ev => {
      market.processEvent(ev)
      f(market)
    })
  }

}

object OrderReplay {

  def main(args: Array[String]) {

    val url = TickDatabase.url(args)
    Database.forURL(url, driver = "com.mysql.jdbc.Driver") withSession {

      val allEventsByTime =
        Query(events).sortBy(_.messageSequenceNumber).sortBy(_.timeStamp)

      val timeSeries =
        for {
          market <- new OrderFlow(allEventsByTime.list)
        } yield (market.lastChanged, market.midPrice)

      for ((t, price) <- timeSeries) {
        println(t.get.getTicks() + "\t" + (price match {
          case Some(p) => p.toString()
          case None => "NaN"
        }))
      }

    }

  }
}

package org.ccfea.tickdata.storage

import rawdata._
import grizzled.slf4j.Logger
import org.ccfea.tickdata.event.{EventType, Event}
import org.ccfea.tickdata.storage.rawdata.lse.{OrderDetailRaw, OrderHistoryRaw, TradeReportRaw}
import org.ccfea.tickdata.order.TradeDirection

/**
 * Parse the raw data from LSE and convert it to a sequence of tick Events.
 *
 * (c) Steve Phelps 2013
 */

trait DataLoader {

  val logger = Logger(classOf[DataLoader])

  val batchSize: Int

  def run(): Unit

  def insertData(parsedEvents: Seq[Event]): Int

  def parseEvent(rawEvent: HasDateTime): Event = {

    logger.debug("Raw event = " + rawEvent)

    implicit def orderActionTypeToEventType(orderActionType: String): EventType.Value = {
      orderActionType match {
        case "D" => EventType.OrderDeleted
        case "E" => EventType.OrderExpired
        case "P" => EventType.OrderMatched
        case "M" => EventType.OrderFilled
        case "T" => EventType.TransactionLimit
      }
    }

    implicit def buySellIndToTradeDirection(buySellInd: String): TradeDirection.Value = {
      buySellInd match {
        case "B" => TradeDirection.Buy
        case "S" => TradeDirection.Sell
      }
    }

    rawEvent match {

      case OrderHistoryRaw(orderCode, orderActionType, matchingOrderCode,
                              tradeSize, tradeCode, tiCode, countryOfRegister,
                              currencyCode, marketSegmentCode,
                              aggregateSize, buySellInd,
                              marketMechanismType,
                              messageSequenceNumber, date, time) =>

          Event(None, orderActionType, messageSequenceNumber,
            rawEvent.timeStamp, tiCode, marketSegmentCode, currencyCode,
            Some(marketMechanismType), Some(aggregateSize), Some(buySellInd),
            Some(orderCode), Some(tradeSize), None, None, None, None, None,
            matchingOrderCode, tradeCode,
            None, None, None)


      case OrderDetailRaw(orderCode, marketSegmentCode, marketSectorCode,
                          tiCode, countryOfRegister, currencyCode,
                          participantCode, buySellInd, marketMechanismGroup,
                          marketMechanismType, price, aggregateSize,
                          singleFillInd, broadcastUpdateAction, date, time,
                          messageSequenceNumber) =>

          Event(None, EventType.OrderSubmitted, messageSequenceNumber,
            rawEvent.timeStamp, tiCode, marketSegmentCode, currencyCode,
            Some(marketMechanismType), Some(aggregateSize), Some(buySellInd),
            Some(orderCode), None, Some(broadcastUpdateAction),
            Some(marketSectorCode), Some(marketMechanismGroup), Some(price), Some(singleFillInd),
            None, None,
            None, None, None)

      case TradeReportRaw(messageSequenceNumber, tiCode, marketSegmentCode,
                          countryOfRegister, currencyCode, tradeCode,
                          tradePrice, tradeSize, date, time,
                          broadcastUpdateAction, tradeTypeInd,
                          tradeTimeInd, bargainConditions, convertedPriceInd,
                          publicationDate, publicationTime) =>

          Event(None, EventType.Transaction,
            messageSequenceNumber, rawEvent.timeStamp,
            tiCode, marketSegmentCode, currencyCode,
            None, None, None, None,
            Some(tradeSize), Some(broadcastUpdateAction),
            None, None, tradePrice, None,
            None, None,
            tradeCode, Some(tradeTimeInd), Some(convertedPriceInd))
    }

  }


}


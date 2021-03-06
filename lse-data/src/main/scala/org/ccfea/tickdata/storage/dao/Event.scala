package org.ccfea.tickdata.storage.dao

import java.util.Date

import grizzled.slf4j.Logger
import org.ccfea.tickdata.event._
import org.ccfea.tickdata.order._

/**
 * A non-relational representation of an event that has occurred in the exchange.
 * A time-ordered sequence of Events can be replayed through a simulator in order to
 * reconstruct the state of the market at any given time.  Events are represented as a flat
 * tuple in order to maintain high-performance and avoid complicated joins across many tables.
 * They can be converted to an object-oriented representation using the method
 * {@link org.ccfea.tickdata.event.Event#tick}.
 *
 * (c) Steve Phelps 2013
 */
case class Event(eventID: Option[Long], eventType: EventType.Value, messageSequenceNumber: Long, timeStamp: Long,
  tiCode: String, marketSegmentCode: String, currencyCode: String,
  marketMechanismType: Option[MarketMechanismType.Value], aggregateSize: Option[Long],
  tradeDirection: Option[TradeDirection.Value], orderCode: Option[String], tradeSize: Option[Long],
  broadcastUpdateAction: Option[String], marketSectorCode: Option[String], marketMechanismGroup: Option[String],
  price: Option[BigDecimal], singleFillInd: Option[String], matchingOrderCode: Option[String],
  resultingTradeCode: Option[String], tradeCode: Option[String], tradeTimeInd: Option[String],
  convertedPriceInd: Option[String]
) {

  def tick: TickDataEvent = {

    this.eventType match {

      case EventType.OrderSubmitted =>

        val order = marketMechanismType.get match {
          case MarketMechanismType.LimitOrder =>
            new LimitOrder(orderCode.get, aggregateSize.get, tradeDirection.get, price.get, new Trader())
          case MarketMechanismType.MarketOrder =>
            new MarketOrder(orderCode.get, aggregateSize.get, tradeDirection.get, new Trader())
          case _ =>
            new OtherOrder(orderCode.get, marketMechanismType.get, new Trader())
        }
        val date = new Date(timeStamp)
        val orderSubmittedEvent = new OrderSubmittedEvent(date, messageSequenceNumber, tiCode, order)
        broadcastUpdateAction match {
          case Some("F") =>
            val markerEvent = new StartOfDataMarker(date, messageSequenceNumber, tiCode)
            new MultipleEvent(List(markerEvent, orderSubmittedEvent))
          case _ =>
            orderSubmittedEvent
        }

      case EventType.OrderDeleted | EventType.OrderExpired | EventType.TransactionLimit =>
        new OrderRemovedEvent(new Date(timeStamp), messageSequenceNumber, tiCode, new Order(orderCode.get))

      case EventType.OrderFilled =>
        filterMissingMatchingOrderCode{
          () => new OrderFilledEvent(new Date(timeStamp), messageSequenceNumber, tiCode,
                                        new Order(orderCode.get), new Order(matchingOrderCode.get))
        }

      case EventType.OrderMatched =>
        filterMissingMatchingOrderCode{
          () => new OrderMatchedEvent(new Date(timeStamp), messageSequenceNumber, tiCode,
                                        new Order(orderCode.get), new Order(matchingOrderCode.get))
//                                        resultingTradeCode.get, tradeSize.get)
//                                        tradeSize.get)
        }

      case EventType.Transaction =>
        new TransactionEvent(new Date(timeStamp), messageSequenceNumber, tiCode, tradeCode.get, price.get,
          tradeSize.get, orderCode, matchingOrderCode)

      case EventType.OrderRevised =>
        new OrderRevisedEvent(new Date(timeStamp), messageSequenceNumber, tiCode, new Order(orderCode.get), price.get,
          aggregateSize.get, tradeDirection.get)

      case EventType.None =>
        new NoopEvent(new Date(timeStamp), messageSequenceNumber, tiCode)
    }
  }

  /**
    * If the matchingOrderCode is missing for OrderFilled or OrderMatchedEvents, then
    *  return an OrderRemovedEvent, otherwise return the supplied argument intact.
    *
    * @param createMatchingEvent    A function to create the event to return
    *                                  if the matchingOrderCode is present.
    * @return                       Either the matching event, or an OrderRemovedEvent.
    */
  def filterMissingMatchingOrderCode(createMatchingEvent: () => TickDataEvent) = {
    this.matchingOrderCode match {
      case Some(orderCode) => createMatchingEvent()
      case None =>
        new OrderRemovedEvent(new Date(timeStamp), messageSequenceNumber, tiCode, new Order(orderCode.get))
    }
  }

  implicit def nonRelationalToObjectOriented(x: Event): TickDataEvent = x.tick
}

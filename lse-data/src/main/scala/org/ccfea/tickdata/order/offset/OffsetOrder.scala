package org.ccfea.tickdata.order.offset

import net.sourceforge.jasa.market.Price
import org.ccfea.tickdata.order.{LimitOrder, OrderWithVolume, Trader}
import org.ccfea.tickdata.simulator.Quote

/**
 * A virtual limit-order used for simulation studies in which
 * the price of the order is specified as an offset from the
 * current best price(s) at the time the order was placed.
 * This allows the order to be replayed as an offset for
 * random permutation experiments.
 *
 * (C) Steve Phelps 2014
 */
abstract class OffsetOrder(val limitOrder: LimitOrder, val initialQuote: Quote) extends OrderWithVolume {

  val orderCode = limitOrder.orderCode
  val aggregateSize = limitOrder.aggregateSize
  val tradeDirection = limitOrder.tradeDirection
  val originalPrice = limitOrder.price
  val trader = limitOrder.trader

  val offset: Double = bestPrice(initialQuote) match {
      //TODO
    case Some(best) => round((limitOrder.price - round(best.doubleValue())).toDouble)
    case None => 0.0
  }

  def price(quote: Quote): BigDecimal =
    bestPrice(quote) match {
      case None => originalPrice
        //TODO
      case Some(p) => round(p.doubleValue()) + offset
    }

  def round(p: Double): Double = {
    (p * 1000.0) / 1000.0
  }

  def bestPrice(quote: Quote): Option[Price]

  def toLimitOrder(quote: Quote) = {
    new LimitOrder(orderCode, aggregateSize, tradeDirection, price(quote), new Trader())
  }

}

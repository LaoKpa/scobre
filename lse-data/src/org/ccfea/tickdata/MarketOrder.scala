package org.ccfea.tickdata

/**
 * (C) Steve Phelps 2013
 */
case class MarketOrder(orderCode: String, aggregateSize: Long, tradeDirection: TradeDirection.Value) extends OrderWithVolume

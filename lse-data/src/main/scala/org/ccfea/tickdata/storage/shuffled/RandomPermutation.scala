package org.ccfea.tickdata.storage.shuffled

import org.ccfea.tickdata.event.TickDataEvent
import org.ccfea.tickdata.storage.rawdata.HasDateTime
import org.ccfea.tickdata.storage.shuffled.swapper.{Swapper, TickSwapper}
import scala.util.Random

/**
 * A random permutation of tick objects.
 *
 * (C) Steve Phelps 2014
 */
class RandomPermutation(val source: Seq[TickDataEvent], val proportion: Double, val windowSize: Int = 1,
                         val swapper: Swapper[_] = new TickSwapper())
      extends Seq[TickDataEvent] {

  val n: Int = source.length - (source.length % windowSize)
  var ticks: Array[TickDataEvent] = new Array[TickDataEvent](n)

  shuffleTicks()

  def initialise(): Unit = {
    source.copyToArray(ticks, 0, n)
  }

  def shuffleTicks(): Unit = {
    initialise()
    if (proportion > 0.0) {
      val numWindows = ticks.length / windowSize
      val numShuffledWindows = math.floor(proportion * numWindows).toInt
      val windowsToShuffle = sampleWithoutReplacement(numShuffledWindows, numWindows)
      val shuffledPositions = Random.shuffle(windowsToShuffle)
      for(i <- 0 until windowsToShuffle.length) {
        swapWindows(windowsToShuffle(i), shuffledPositions(i))
      }
     }
  }

  def swapWindows(window1: Int, window2: Int) = {
    for(i <- 0 until windowSize) {
      val a = window1 * windowSize + i
      val b = window2 * windowSize + i
      swap(a, b)
    }
  }

  def swap(a: Int, b: Int) = swapper.swapAttributes(a, b, this)

  def sampleWithoutReplacement(n: Int, N: Int): Seq[Int] = {
    var t: Int = 0
    var m: Int = 0
    val samples = new Array[Int](n)
    while (m < n) {
      if ((N - t) * Random.nextDouble() >= n - m) {
        t = t + 1
      } else {
        samples(m) = t
        t = t + 1
        m = m + 1
      }
    }
    samples
  }

  override def iterator: Iterator[TickDataEvent] = ticks.iterator
  override def length: Int = ticks.length
  override def apply(i: Int): TickDataEvent = ticks.apply(i)
  def update(i: Int, x: TickDataEvent) = ticks.update(i, x)
}

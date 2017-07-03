/*
BPFilter.scala

Top level code for pfilter blog post

 */

package montescala

import TypeClasses._
import BPFilter._

object BPFExample {

  // Main method
  def main(args: Array[String]): Unit = {
    println("Demo running a particle filter - takes a while to run")
    arTest
    println("Finished.")
  }

  // Simple test for an AR(1) model
  def arTest: Unit = {
    import breeze.linalg._
    import breeze.stats.distributions._
    println("AR(1) test start")
    // simulate some data from an AR(1) model with noise
    val inNoise = Gaussian(0.0, 1.0).sample(99)
    val state = DenseVector(inNoise.scanLeft(0.0)((s, i) => 0.8 * s + i).toArray)
    val noise = DenseVector(Gaussian(0.0, 2.0).sample(100).toArray)
    val data = (state + noise).toArray.toList
    import breeze.plot._
    val f = Figure()
    val p0 = f.subplot(0)
    val idx = linspace(1, 100, 100)
    p0 += plot(idx, state)
    p0 += plot(idx, data, '.')
    p0.xlabel = "Time"
    p0.ylabel = "Value"
    // now try to recover autoregression coefficient
    implicit val dState = new State[Double] {}
    implicit val dObs = new Observation[Double] {}
    implicit val dPar = new Parameter[Double] {}
    import scala.collection.parallel.immutable.ParVector
    val mll = pfMll(
      (th: Double) => Gaussian(0.0, 10.0).sample(10000).toVector.par,
      (th: Double) => (s: Double) => Gaussian(th * s, 1.0).draw,
      (th: Double) => (s: Double, o: Double) => Gaussian(s, 2.0).logPdf(o),
      (zc: ParVector[(Double, Double)], srw: Double, l: Int) => resampleMN(zc,srw,l),
      data
    )
    val x = linspace(0.0, 0.99, 100)
    val y = x map (mll(_))
    //println(y)
    val p1 = f.subplot(2, 1, 1)
    p1 += plot(x, y)
    p1.xlabel = "theta"
    p1.ylabel = "mll"
    f.saveas("plot.png")
    println("AR(1) test finish")
  }

}

// eof


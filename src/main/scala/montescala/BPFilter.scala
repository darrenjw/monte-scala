/*
BPFilter.scala

Top level code for pfilter blog post

 */

package montescala

import scala.collection.GenTraversable
import TypeClasses._
import GenericColl.ops._

object BPFilter {

  // Single step of a bootstrap particle filter
  def update[S: State, O: Observation, C[_]: GenericColl](
    dataLik: (S, O) => LogLik,
    stepFun: S => S,
    resample: (C[(Double, S)], Double, Int) => C[S]
  )(
    x: C[S], o: O): (LogLik, C[S]
  ) = {
    val xp = x map (stepFun(_))
    val lw = xp map (dataLik(_, o))
    val max = lw reduce (math.max(_, _))
    val rw = lw map (lwi => math.exp(lwi - max))
    val srw = rw reduce (_ + _)
    val l = rw.length
    val z = rw zip xp
    val rx = resample(z, srw, l)
    (max + math.log(srw / l), rx)
  }

  // Simple Poisson resampling - unstable for long time series
  def resamplePoisson[S: State, C[_]: GenericColl](
    zc: C[(Double,S)],
    srw: Double,
    l: Int
  ): C[S] = {
    import breeze.stats.distributions.Poisson
    zc flatMap { case (rwi, xpi) =>
      Vector.fill(Poisson(rwi * l / srw).draw)(xpi) }
  }

  // Multinomial resampling - serial implemetation for now
  // TODO: Replace with a parallel implementation
  def resampleMN[S: State, C[_]: GenericColl](
    zc: C[(Double,S)],
    srw: Double,
    l: Int
  ): C[S] = {
    import breeze.stats.distributions.Binomial
    val w = zc map {case (rwi, xpi) => rwi}
    val x = zc map {case (rwi, xpi) => xpi}
    val counts =  w.scanLeft((l, srw))((p, w) =>
      (if (p._1 > 0) p._1 - Binomial(p._1, w / p._2).draw else 0,
        p._2 - w)).
      drop(1).
      scanLeft((l, 0))((a, b) => (b._1, a._1 - b._1)).
      drop(1).
      map(_._2)
    val cx = counts zip x
    cx flatMap {case (ci, xi) => Vector.fill(ci)(xi)}
  }


  // Run a bootstrap particle filter over a collection of observations
  def pFilter[S: State, O: Observation, C[_]: GenericColl, D[O] <: GenTraversable[O]](
    x0: C[S],
    data: D[O],
    dataLik: (S, O) => LogLik,
    stepFun: S => S,
    resample: (C[(Double, S)], Double, Int) => C[S]
  ): (LogLik, C[S]) = {
    val updater = update[S, O, C](dataLik, stepFun, resample) _
    data.foldLeft((0.0, x0))((prev, o) => {
      val (oll, ox) = prev
      val (ll, x) = updater(ox, o)
      (oll + ll, x)
    })
  }

  // Marginal log likelihood estimation
  def pfMll[S: State, P: Parameter, O: Observation, C[_]: GenericColl, D[O] <: GenTraversable[O]](
    simX0: P => C[S],
    stepFun: P => S => S,
    dataLik: P => (S, O) => LogLik,
    resample: (C[(Double, S)], Double, Int) => C[S],
    data: D[O]
  ): (P => LogLik) =
    (th: P) =>
  pFilter(simX0(th), data, dataLik(th), stepFun(th), resample)._1



}


// eof


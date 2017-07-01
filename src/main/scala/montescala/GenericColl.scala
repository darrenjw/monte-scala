/*
GenericColl.scala

Typeclasses useful for Monte Carlo, especially the GenericColl typeclass

 */

package montescala

import scala.collection.parallel.immutable.ParVector
import scala.collection.GenTraversable
import simulacrum._

object TypeClasses {

  // Hardcode LogLik type
  type LogLik = Double
  // Use blank typeclasses for State, Observation, and Parameter
  trait State[T]
  trait Observation[T]
  trait Parameter[T]

  // My generic collection typeclass
  @typeclass trait GenericColl[C[_]] {
    def map[A, B](ca: C[A])(f: A => B): C[B]
    def reduce[A](ca: C[A])(f: (A, A) => A): A
    def scan[A](ca: C[A])(z: A)(f: (A, A) => A): C[A]
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: C[A])(f: A => D[B]): C[B]
    def zip[A, B](ca: C[A])(cb: C[B]): C[(A, B)]
    def length[A](ca: C[A]): Int
  }

  // import GenericColl.ops._

  // Instance for Vector
  implicit val vGC: GenericColl[Vector] = new GenericColl[Vector] {
    def map[A, B](ca: Vector[A])(f: A => B): Vector[B] = ca map f
    def reduce[A](ca: Vector[A])(f: (A, A) => A): A = ca reduce f
    def scan[A](ca: Vector[A])(z: A)(f: (A, A) => A): Vector[A] = ca.scan(z)(f)
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: Vector[A])(f: A => D[B]): Vector[B] = ca flatMap f
    def zip[A, B](ca: Vector[A])(cb: Vector[B]): Vector[(A, B)] = ca zip cb
    def length[A](ca: Vector[A]) = ca.length
  }

  // Instance for ParVector
  implicit val pvGC: GenericColl[ParVector] = new GenericColl[ParVector] {
    def map[A, B](ca: ParVector[A])(f: A => B): ParVector[B] = ca map f
    def reduce[A](ca: ParVector[A])(f: (A, A) => A): A = ca reduce f
    def scan[A](ca: ParVector[A])(z: A)(f: (A, A) => A): ParVector[A] = ca.scan(z)(f)
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: ParVector[A])(f: A => D[B]): ParVector[B] = ca flatMap f
    def zip[A, B](ca: ParVector[A])(cb: ParVector[B]): ParVector[(A, B)] = ca zip cb
    def length[A](ca: ParVector[A]) = ca.length
  }

  // TODO: Implementation for Spark RDDs - in another project


  // Add a .thin method to Stream

  // First define a Thinnable typeclass
  @typeclass
  trait Thinnable[F[_]] {
    def thin[T](f: F[T], th: Int): F[T]
  }

  // A thinnable instance for Stream
  implicit val streamThinnable: Thinnable[Stream] =
    new Thinnable[Stream] {
      def thin[T](s: Stream[T],th: Int): Stream[T] = {
        val ss = s.drop(th-1)
        if (ss.isEmpty) Stream.empty else
          ss.head #:: thin(ss.tail, th)
      }
    }


}

// eof


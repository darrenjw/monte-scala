/*
GenericColl.scala

 */

package montescala

object TypeClasses {

  import scala.collection.parallel.immutable.ParVector
  import scala.collection.GenTraversable

  // Hardcode LogLik type
  type LogLik = Double
  // Use blank typeclasses for State, Observation, and Parameter
  trait State[T]
  trait Observation[T]
  trait Parameter[T]

  // My generic collection typeclass
  trait GenericColl[C[_]] {
    def map[A, B](ca: C[A])(f: A => B): C[B]
    def reduce[A](ca: C[A])(f: (A, A) => A): A
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: C[A])(f: A => D[B]): C[B]
    def zip[A, B](ca: C[A])(cb: C[B]): C[(A, B)]
    def length[A](ca: C[A]): Int
  }
  // Syntax for the typeclass
  implicit class GenericCollSyntax[A, C[A]](value: C[A]) {
    def map[B](f: A => B)(implicit inst: GenericColl[C]): C[B] = inst.map(value)(f)
    def reduce(f: (A, A) => A)(implicit inst: GenericColl[C]): A = inst.reduce(value)(f)
    def flatMap[B, D[B] <: GenTraversable[B]](f: A => D[B])(implicit inst: GenericColl[C]): C[B] = inst.flatMap(value)(f)
    def zip[B](cb: C[B])(implicit inst: GenericColl[C]): C[(A, B)] = inst.zip(value)(cb)
    def length(implicit inst: GenericColl[C]): Int = inst.length(value)
  }

  // Implementation for Vector
  implicit val vGC: GenericColl[Vector] = new GenericColl[Vector] {
    def map[A, B](ca: Vector[A])(f: A => B): Vector[B] = ca map f
    def reduce[A](ca: Vector[A])(f: (A, A) => A): A = ca reduce f
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: Vector[A])(f: A => D[B]): Vector[B] = ca flatMap f
    def zip[A, B](ca: Vector[A])(cb: Vector[B]): Vector[(A, B)] = ca zip cb
    def length[A](ca: Vector[A]) = ca.length
  }

  // Implementation for ParVector
  implicit val pvGC: GenericColl[ParVector] = new GenericColl[ParVector] {
    def map[A, B](ca: ParVector[A])(f: A => B): ParVector[B] = ca map f
    def reduce[A](ca: ParVector[A])(f: (A, A) => A): A = ca reduce f
    def flatMap[A, B, D[B] <: GenTraversable[B]](ca: ParVector[A])(f: A => D[B]): ParVector[B] = ca flatMap f
    def zip[A, B](ca: ParVector[A])(cb: ParVector[B]): ParVector[(A, B)] = ca zip cb
    def length[A](ca: ParVector[A]) = ca.length
  }

  // TODO: Implementation for Spark RDDs - in another project

}

// eof


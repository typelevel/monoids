package io.chrisdavenport.monoids

import cats._
final case class Sum[A](getSum: A) extends AnyVal
object Sum {
  implicit def sumNumeric[A](implicit T: Numeric[A]): Monoid[Sum[A]] = new Monoid[Sum[A]]{
    def empty : Sum[A] = Sum(T.zero)
    def combine(x: Sum[A], y: Sum[A]):Sum[A]= Sum(T.plus(x.getSum, y.getSum))
  }
}

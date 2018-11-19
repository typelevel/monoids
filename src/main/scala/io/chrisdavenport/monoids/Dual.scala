package io.chrisdavenport.monoids

import cats.Monoid
final case class Dual[A](getDual: A) extends AnyVal
object Dual {
  implicit def dualMonoid[A: Monoid]: Monoid[Dual[A]] = new Monoid[Dual[A]]{
    def empty: Dual[A] = Dual(Monoid[A].empty)
    def combine(x: Dual[A], y: Dual[A]): Dual[A] = 
      Dual(Monoid[A].combine(y.getDual, x.getDual))
  }
}
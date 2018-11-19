package io.chrisdavenport.monoids

import cats.Monoid

final case class First[A](getFirst: Option[A]) extends AnyVal
object First {
  implicit def firstMonoid[A]: Monoid[First[A]] = new Monoid[First[A]]{
    def empty: First[A] = First(Option.empty[A])
    def combine(x: First[A], y: First[A]): First[A] = First(x.getFirst.orElse(y.getFirst))
  }
}
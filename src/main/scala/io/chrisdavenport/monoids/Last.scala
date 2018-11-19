package io.chrisdavenport.monoids

import cats.Monoid

final case class Last[A](getLast: Option[A]) extends AnyVal
object Last {
  implicit def lastMonoid[A]: Monoid[Last[A]] = new Monoid[Last[A]]{
    def empty: Last[A] = Last(Option.empty[A])
    def combine(x: Last[A], y: Last[A]): Last[A] = Last(y.getLast.orElse(x.getLast))
  }
}
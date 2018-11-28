package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Any(getAny: Boolean) extends AnyVal
object Any {
  implicit val anyMonoid: Monoid[Any] = new Monoid[Any]{
    def empty: Any = Any(false)
    def combine(x: Any,y: Any): Any = Any(x.getAny || y.getAny)
  }
  implicit val anyShow : Show[All] = Show.fromToString
  implicit val anyOrd: Order[All] = Order.by(_.getAll)
}
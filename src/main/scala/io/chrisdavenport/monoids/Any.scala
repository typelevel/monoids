package io.chrisdavenport.monoids

import cats._
import cats.kernel.CommutativeMonoid
import cats.implicits._

final case class Any(getAny: Boolean) extends AnyVal
object Any {
  implicit val anyMonoid: CommutativeMonoid[Any] = new CommutativeMonoid[Any]{
    def empty: Any = Any(false)
    def combine(x: Any,y: Any): Any = Any(x.getAny || y.getAny)
  }
  implicit val anyShow : Show[Any] = Show.fromToString
  implicit val anyOrd: Order[Any] = Order.by(_.getAny)
}
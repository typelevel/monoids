package io.chrisdavenport.monoids

import cats._
import cats.kernel.BoundedSemilattice
import cats.implicits._

final case class All(getAll: Boolean) extends AnyVal
object All {
  def all[F[_]: Foldable](fa: F[All]): All = All(fa.forall(_.getAll))

  implicit val allMonoid: BoundedSemilattice[All] = new BoundedSemilattice[All] {
    val empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }

  implicit val allShow: Show[All] = Show.fromToString
  implicit val allOrd: Order[All] = Order.by(_.getAll)
}

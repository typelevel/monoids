package org.typelevel.monoids

import cats._
import cats.kernel.BoundedSemilattice
import cats.syntax.all._

private[monoids] trait AllInstances {
  def all[F[_]: Foldable](fa: F[All]): All = All(fa.forall(_.getAll))

  implicit val allMonoid: BoundedSemilattice[All] = new BoundedSemilattice[All] {
    val empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }

  implicit val allShow: Show[All] = Show.show[All](all => s"All(${all.getAll.show})")
  implicit val allOrd: Order[All] = Order.by(_.getAll)
}

package io.chrisdavenport.monoids

import cats._

final case class All(getAll: Boolean) extends AnyVal
object All {
  implicit val allMonoid: Monoid[All] = new Monoid[All]{
    def empty: All = All(true)
    def combine(x: All, y: All): All = All(x.getAll && y.getAll)
  }
}
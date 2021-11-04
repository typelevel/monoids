package org.typelevel.semigroups

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class MinSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Min", OrderTests[Min[Int]].order)
  checkAll("Min", BoundedSemilatticeTests[Min[Int]].boundedSemilattice)
  checkAll("Min", MonadTests[Min].monad[Int, Int, Int])
  checkAll(
    "Min",
    NonEmptyTraverseTests[Min].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Min", DistributiveTests[Min].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Min(true).show, "Min(true)")
    assertEquals(Min(false).show, "Min(false)")
  }

  test("returns Smaller") {
    val first = Min(1)
    val second = Min(3)
    assertEquals(first |+| second, first)
  }
}

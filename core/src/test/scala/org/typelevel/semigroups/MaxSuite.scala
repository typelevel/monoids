package org.typelevel.semigroups

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class MaxSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Max", OrderTests[Max[Int]].order)
  checkAll("Max", BoundedSemilatticeTests[Max[Int]].boundedSemilattice)
  checkAll("Max", MonadTests[Max].monad[Int, Int, Int])
  checkAll(
    "Max",
    NonEmptyTraverseTests[Max].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Max", DistributiveTests[Max].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Max(true).show, "Max(true)")
    assertEquals(Max(false).show, "Max(false)")
  }

  test("returns Larger") {
    val first = Max(1)
    val second = Max(3)
    assertEquals(first |+| second, second)
  }
}

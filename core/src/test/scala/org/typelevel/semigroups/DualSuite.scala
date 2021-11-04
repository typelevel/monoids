package org.typelevel.semigroups

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class DualSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Dual", OrderTests[Dual[Int]].order)
  checkAll("Dual", SemigroupTests[Dual[Int]].semigroup)
  checkAll("Dual", MonadTests[Dual].monad[Int, Int, Int])
  checkAll(
    "Dual",
    NonEmptyTraverseTests[Dual].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Dual", DistributiveTests[Dual].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Dual(true).show, "Dual(true)")
    assertEquals(Dual(false).show, "Dual(false)")
  }

  test("inverse default") {
    val first = "Hello "
    val second = "World"
    val expected = "Hello World"

    assertEquals(first |+| second, expected)
    assertEquals((Dual(second) |+| Dual(first)).getDual, expected)
  }
}

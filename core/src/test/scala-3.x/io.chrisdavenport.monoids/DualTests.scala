package io.chrisdavenport.monoids

import cats._
import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class DualTests extends munit.DisciplineSuite with MonoidsArbitraries with ExtraMonoidsArbitraries {
  checkAll("Dual", EqTests[Dual[Int]].eqv)
  checkAll("Dual", OrderTests[Dual[Int]].order)
  checkAll("Dual", MonoidTests[Dual[Int]].monoid)
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

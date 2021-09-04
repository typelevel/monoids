package io.chrisdavenport.monoids

import cats._
import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class SumTests extends munit.DisciplineSuite with MonoidsArbitraries with ExtraMonoidsArbitraries {
  checkAll("Sum", EqTests[Sum[Int]].eqv)
  checkAll("Sum", OrderTests[Sum[Int]].order)
  checkAll("Sum", CommutativeMonoidTests[Sum[Int]].commutativeMonoid)
  checkAll("Sum", CommutativeMonadTests[Sum].commutativeMonad[Int, Int, Int])
  checkAll(
    "Sum",
    NonEmptyTraverseTests[Sum]
      .nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll(
    "Sum",
    DistributiveTests[Sum].distributive[Int, Int, Int, Option, Id]
  )

  test("show") {
    assertEquals(Sum(true).show, "Sum(true)")
    assertEquals(Sum(false).show, "Sum(false)")
  }

  test("addition") {
    assertEquals(List(1, 2, 3, 6).foldMap(Sum(_)), Sum(12))
  }

  test("sum") {
    assertEquals(Sum.sum(List(1, 2, 3, 6)), 12)
  }
}

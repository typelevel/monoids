package io.chrisdavenport.monoids

import cats._
import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class ProductTests
    extends munit.DisciplineSuite
    with MonoidsArbitraries
    with ExtraMonoidsArbitraries {
  checkAll("Product", EqTests[Product[Int]].eqv)
  checkAll("Product", OrderTests[Product[Int]].order)
  checkAll("Product", CommutativeMonoidTests[Product[Int]].commutativeMonoid)
  checkAll(
    "Product",
    CommutativeMonadTests[Product].commutativeMonad[Int, Int, Int]
  )
  checkAll(
    "Product",
    NonEmptyTraverseTests[Product]
      .nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll(
    "Product",
    DistributiveTests[Product].distributive[Int, Int, Int, Option, Id]
  )

  test("show") {
    assertEquals(Product(true).show, "Product(true)")
    assertEquals(Product(false).show, "Product(false)")
  }

  test("multiplication") {
    assertEquals(List(1, 2, 3, 6).foldMap(Product(_)), Product(36))
  }

  test("product") {
    assertEquals(Product.product(List(1, 2, 3, 6)), 36)
  }
}

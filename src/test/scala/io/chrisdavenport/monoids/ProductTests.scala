package io.chrisdavenport.monoids

import cats._
// import cats.implicits._
import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class ProductTests extends CatsSuite with MonoidsArbitraries {
  checkAll("Product", OrderTests[Product[Int]].order)
  checkAll("Product", CommutativeMonoidTests[Product[Int]].commutativeMonoid)
  checkAll("Product", CommutativeMonadTests[Product].commutativeMonad)
  checkAll("Product", NonEmptyTraverseTests[Product].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option])
  checkAll("Product", DistributiveTests[Product].distributive[Int, Int, Int, Option, Id])


  test("show"){
    Product(true).show shouldEqual "Product(true)"
    Product(false).show shouldEqual "Product(false)"
  }
}
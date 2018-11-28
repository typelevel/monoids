package io.chrisdavenport.monoids

import cats._
// import cats.implicits._
import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class SumTests extends CatsSuite with MonoidsArbitraries {
  checkAll("Sum", OrderTests[Sum[Int]].order)
  checkAll("Sum", CommutativeMonoidTests[Sum[Int]].commutativeMonoid)
  checkAll("Sum", CommutativeMonadTests[Sum].commutativeMonad)
  checkAll("Sum", NonEmptyTraverseTests[Sum].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option])
  checkAll("Sum", DistributiveTests[Sum].distributive[Int, Int, Int, Option, Id])


  test("show"){
    Sum(true).show shouldEqual "Sum(true)"
    Sum(false).show shouldEqual "Sum(false)"
  }
}

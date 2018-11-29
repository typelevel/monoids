package io.chrisdavenport.monoids

import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
// import cats.laws.discipline._

class AllTests extends CatsSuite with MonoidsArbitraries {
  checkAll("All", OrderTests[All].order)
  checkAll("All", CommutativeMonoidTests[All].commutativeMonoid)
  
  test("show"){
    All(true).show shouldEqual "All(true)"
    All(false).show shouldEqual "All(false)"
  }

  test("Any false is false"){
    List(true, true, true).foldMap(All(_)) shouldEqual All(true)
    List(true, false, true).foldMap(All(_)) shouldEqual All(false)
  }

}
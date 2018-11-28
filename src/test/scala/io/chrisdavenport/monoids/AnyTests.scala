package io.chrisdavenport.monoids

import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
// import cats.laws.discipline._

class AnyTests extends CatsSuite with MonoidsArbitraries {
  checkAll("Any", OrderTests[Any].order)
  checkAll("Any", CommutativeMonoidTests[Any].commutativeMonoid)
  
  test("show"){
    Any(true).show shouldEqual "Any(true)"
    Any(false).show shouldEqual "Any(false)"
  }

  test("Any true is true"){
    List(false, false, false).foldMap(Any(_)) shouldEqual Any(false)
    List(false, true, false).foldMap(Any(_)) shouldEqual Any(true)
  }

}
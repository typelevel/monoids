package org.typelevel.monoids

import cats.implicits._
import cats.kernel.laws.discipline._

class AnyTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Any", OrderTests[Any].order)
  checkAll("Any", BoundedSemilatticeTests[Any].boundedSemilattice)

  test("show") {
    assertEquals(Any(true).show, "Any(true)")
    assertEquals(Any(false).show, "Any(false)")
  }

  test("Any true is true") {
    assertEquals(List(false, false, false).foldMap(Any(_)), Any(false))
    assertEquals(List(false, true, false).foldMap(Any(_)), Any(true))
  }

  test("Any.any for true, false, true") {
    assertEquals(Any.any(List(true, false, true).map(Any(_))), Any(true))
  }

  test("Any.any for true, true, true") {
    assertEquals(Any.any(List(true, true, true).map(Any(_))), Any(true))
  }

  test("Any.any for false, false") {
    assertEquals(Any.any(List(false, false).map(Any(_))), Any(false))
  }

}

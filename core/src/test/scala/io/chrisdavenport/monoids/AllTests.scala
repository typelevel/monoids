package io.chrisdavenport.monoids

import cats.implicits._
import cats.kernel.laws.discipline._

class AllTests extends munit.DisciplineSuite with MonoidsArbitraries {

  checkAll("All", OrderTests[All].order)
  checkAll("All", BoundedSemilatticeTests[All].boundedSemilattice)

  test("show") {
    assertEquals(All(true).show, "All(true)")
    assertEquals(All(false).show, "All(false)")
  }

  test("Any false is false") {
    assertEquals(List(true, true, true).foldMap(All(_)), All(true))
    assertEquals(List(true, false, true).foldMap(All(_)), All(false))
  }

  test("All.all for true, false, true") {
    assertEquals(All.all(List(true, false, true).map(All(_))), All(false))
  }

  test("All.all for true, true, true") {
    assertEquals(All.all(List(true, true, true).map(All(_))), All(true))
  }

  test("All.all for false, false") {
    assertEquals(All.all(List(false, false).map(All(_))), All(false))
  }
}

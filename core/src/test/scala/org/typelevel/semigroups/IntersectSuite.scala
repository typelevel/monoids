package org.typelevel.semigroups

import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class IntersectSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Intersect", EqTests[Intersect[Int]].eqv)
  checkAll("Intersect", SemilatticeTests[Intersect[Int]].semilattice)
  checkAll("Intersect", MonoidKTests[Intersect].monoidK[Int])
  checkAll(
    "Intersect",
    UnorderedTraverseTests[Intersect].unorderedTraverse[Int, Int, Int, Option, Option]
  )

  test("show") {
    assertEquals(Intersect(Set(1, 2)).show, "Intersect(Set(1, 2))")
    assertEquals(Intersect(Set(1)).show, "Intersect(Set(1))")
    assertEquals(Intersect(Set.empty[Int]).show, "Intersect(Set())")
  }

  test("returns Intersect") {
    val first = Intersect(Set(1, 2, 3))
    val second = Intersect(Set(2, 3, 4))
    assertEquals(first |+| second, Intersect(Set(2, 3)))
  }
}

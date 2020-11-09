package io.chrisdavenport.monoids

import cats.implicits._
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class XorTests extends munit.DisciplineSuite with MonoidsArbitraries {
  checkAll("Xor", EqTests[Xor[Int]].eqv)
  checkAll("Xor", CommutativeGroupTests[Xor[Int]].commutativeGroup)
  checkAll("Xor", MonoidKTests[Xor].monoidK[Int])
  checkAll(
    "Xor",
    UnorderedTraverseTests[Xor].unorderedTraverse[Int, Int, Int, Option, Option]
  )

  test("show") {
    assertEquals(Xor(Set(1)).show, "Xor(Set(1))")
  }

  test("symmetric difference") {
    assertEquals(Xor(Set(1, 2, 3)) |+| Xor(Set(3, 4)), Xor(Set(1, 2, 4)))
  }
}

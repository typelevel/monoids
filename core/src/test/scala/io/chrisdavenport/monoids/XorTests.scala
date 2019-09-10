package io.chrisdavenport.monoids

// import cats._
// import cats.implicits._
import cats.tests.CatsSuite
import cats.kernel.laws.discipline._
import cats.laws.discipline._

class XorTests extends CatsSuite with MonoidsArbitraries {
  checkAll("Xor", EqTests[Xor[Int]].eqv)
  checkAll("Xor", CommutativeGroupTests[Xor[Int]].commutativeGroup)
  checkAll("Xor", MonoidKTests[Xor].monoidK)
  checkAll("Xor", UnorderedTraverseTests[Xor].unorderedTraverse[Int, Int, Int, Option, Option])

  test("show"){
    Xor(Set(1)).show shouldEqual "Xor(Set(1))"
  }

  test("symmetric difference"){
    Xor(Set(1, 2, 3)) |+| Xor(Set(3, 4)) shouldEqual Xor(Set(1, 2, 4))
  }
}
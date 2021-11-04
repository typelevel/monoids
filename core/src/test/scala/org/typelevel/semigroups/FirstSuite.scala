package org.typelevel.semigroups

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class FirstSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("First", OrderTests[First[Int]].order)
  checkAll("First", BandTests[First[Int]].band)
  checkAll("First", MonadTests[First].monad[Int, Int, Int])
  checkAll(
    "First",
    NonEmptyTraverseTests[First].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("First", DistributiveTests[First].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(First(true).show, "First(true)")
    assertEquals(First(false).show, "First(false)")
  }

  test("returns first") {
    val first = First("Hello ")
    val second = First("World")
    assertEquals(first |+| second, first)
  }
}

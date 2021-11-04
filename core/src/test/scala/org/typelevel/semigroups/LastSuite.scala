package org.typelevel.semigroups

import cats._
import cats.kernel.laws.discipline._
import cats.laws.discipline._
import cats.syntax.all._

class LastSuite extends munit.DisciplineSuite with SemigroupsArbitraries {
  checkAll("Last", OrderTests[Last[Int]].order)
  checkAll("Last", BandTests[Last[Int]].band)
  checkAll("Last", MonadTests[Last].monad[Int, Int, Int])
  checkAll(
    "Last",
    NonEmptyTraverseTests[Last].nonEmptyTraverse[Option, Int, Int, Int, Int, Option, Option]
  )
  checkAll("Last", DistributiveTests[Last].distributive[Int, Int, Int, Option, Id])

  test("show") {
    assertEquals(Last(true).show, "Last(true)")
    assertEquals(Last(false).show, "Last(false)")
  }

  test("returns Last") {
    val first = Last("Hello ")
    val second = Last("World")
    assertEquals(first |+| second, second)
  }
}

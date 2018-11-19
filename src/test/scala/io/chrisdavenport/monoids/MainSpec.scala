package io.chrisdavenport.monoids

import org.specs2._
import cats.implicits._

object MainSpec extends mutable.Specification {

  "Dual" should {
    "combine must work in the dual of combine" in {
      val first = "Hello "
      val second = "World"
      val expected = "Hello World"
      first.combine(second) must_=== expected 
      Dual(second).combine(Dual(first)).getDual must_=== expected
    }
  }

}
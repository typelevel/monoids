package io.chrisdavenport.monoids.compiling

import cats.implicits._
import io.chrisdavenport.monoids._

object Compilecheck {

  val all: Boolean = List(true, false, true).foldMap(Any(_)).getAny
}
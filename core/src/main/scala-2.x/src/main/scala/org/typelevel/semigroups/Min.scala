package src.main.scala.org.typelevel.semigroups

final case class Min[A](getMin: A) extends AnyVal

object Min extends MinInstances

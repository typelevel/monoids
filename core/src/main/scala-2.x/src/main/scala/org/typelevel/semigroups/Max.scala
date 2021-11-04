package src.main.scala.org.typelevel.semigroups

final case class Max[A](getMax: A) extends AnyVal

object Max extends MaxInstances

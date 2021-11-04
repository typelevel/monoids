package src.main.scala.org.typelevel.semigroups

final case class Dual[A](getDual: A) extends AnyVal

object Dual extends DualInstances

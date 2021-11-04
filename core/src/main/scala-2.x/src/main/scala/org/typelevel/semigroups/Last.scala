package src.main.scala.org.typelevel.semigroups

final case class Last[A](getLast: A) extends AnyVal

object Last extends LastInstances

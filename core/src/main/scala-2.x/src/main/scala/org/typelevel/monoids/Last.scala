package org.typelevel.monoids

final case class Last[A](getLast: Option[A]) extends AnyVal

object Last extends LastInstances

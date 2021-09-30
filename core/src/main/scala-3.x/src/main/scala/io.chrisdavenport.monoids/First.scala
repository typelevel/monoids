package io.chrisdavenport.monoids

opaque type First[A] = Option[A]

object First extends FirstInstances:
  def apply[A](getFirst: Option[A]): First[A] = getFirst
  def unapply[A](arg: First[A]): Option[Option[A]] = Some(arg.getFirst)

extension [A](f: First[A])
  def getFirst: Option[A] = f

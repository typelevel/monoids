package org.typelevel.monoids

opaque type Last[A] = Option[A]

object Last extends LastInstances:
  def apply[A](getLast: Option[A]): Last[A] = getLast
  def unapply[A](arg: Last[A]): Option[Option[A]] = Some(arg.getLast)

extension [A](l: Last[A])
  def getLast: Option[A] = l

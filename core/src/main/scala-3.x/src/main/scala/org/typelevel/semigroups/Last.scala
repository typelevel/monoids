package org.typelevel.semigroups

opaque type Last[A] = A

object Last extends LastInstances:
  def apply[A](getLast: A): Last[A] = getLast
  def unapply[A](arg: Last[A]): Option[A] = Some(arg.getLast)

extension [A](l: Last[A])
  def getLast: A = l

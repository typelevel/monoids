package io.chrisdavenport.monoids

import cats._
import cats.syntax.all._

opaque type Sum[A] = A

object Sum extends SumInstances:
  def apply[A](getSum: A): Sum[A] = getSum
  def unapply[A](arg: Sum[A]): Option[A] = Some(arg.getSum)
  def sum[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Sum[A]]): A = fa.foldMap(Sum(_)).getSum

extension [A](s: Sum[A]) def getSum: A = s

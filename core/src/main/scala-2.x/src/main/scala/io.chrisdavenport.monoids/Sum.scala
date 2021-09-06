package io.chrisdavenport.monoids

import cats._
import cats.syntax.all._

final case class Sum[A](getSum: A) extends AnyVal

object Sum extends SumInstances {
  def sum[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Sum[A]]): A = fa.foldMap(Sum(_)).getSum
}

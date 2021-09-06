package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Product[A](getProduct: A) extends AnyVal

object Product extends ProductInstances {
  def product[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Product[A]]): A =
    fa.foldMap(Product(_)).getProduct
}

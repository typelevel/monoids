package io.chrisdavenport.monoids

import cats._
import cats.kernel.CommutativeMonoid
import cats.implicits._

opaque type Product[A] = A

object Product extends ProductInstances:
  def apply[A](getProduct: A): Product[A] = getProduct
  def unapply[A](arg: Product[A]): Option[A] = Some(arg.getProduct)
  def product[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Product[A]]): A =
    fa.foldMap(Product(_)).getProduct

extension [A](p: Product[A]) def getProduct: A = p

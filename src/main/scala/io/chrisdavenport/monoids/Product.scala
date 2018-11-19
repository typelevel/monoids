package io.chrisdavenport.monoids

import cats._

final case class Product[A](getProduct: A) extends AnyVal
object Product {
  implicit def sumNumeric[A](implicit T: Numeric[A]): Monoid[Product[A]] = new Monoid[Product[A]]{
    def empty : Product[A] = Product(T.one)
    def combine(x: Product[A], y: Product[A]): Product[A]= Product(T.times(x.getProduct, y.getProduct))
  }
}
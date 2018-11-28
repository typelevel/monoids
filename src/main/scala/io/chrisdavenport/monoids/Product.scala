package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Product[A](getProduct: A) extends AnyVal
object Product extends ProductInstances

private[monoids] trait ProductInstances extends ProductInstances1 {
  implicit def sumNumeric[A](implicit T: Numeric[A]): Monoid[Product[A]] = new Monoid[Product[A]]{
    def empty : Product[A] = Product(T.one)
    def combine(x: Product[A], y: Product[A]): Product[A]= Product(T.times(x.getProduct, y.getProduct))
  }

  implicit def productShow[A: Show]: Show[Product[A]] = 
    Show.show[Product[A]](productA => show"Product(${productA.getProduct})")

  implicit def productOrder[A: Order]: Order[Product[A]] =
    Order.by(_.getProduct)
  implicit val productMonad: Monad[Product] = new Monad[Product]{
    def pure[A](a: A): Product[A] = Product(a)
    def flatMap[A,B](fa: Product[A])(f: A => Product[B]): Product[B] = f(fa.getProduct)

    @scala.annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => Product[Either[A,B]]): Product[B] =
      f(a) match {
        case Product(Left(a)) => tailRecM(a)(f)
        case Product(Right(b)) => Product(b)
      }
  }
}

private[monoids] trait ProductInstances1 {
  implicit def ProductEq[A: Eq]: Eq[Product[A]] = Eq.by(_.getProduct)
}
package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class Product[A](getProduct: A) extends AnyVal
object Product extends ProductInstances

private[monoids] trait ProductInstances extends ProductInstances1 {
  implicit def productNumericMonoid[A](implicit T: Numeric[A]): Monoid[Product[A]] = new Monoid[Product[A]]{
    def empty : Product[A] = Product(T.one)
    def combine(x: Product[A], y: Product[A]): Product[A]= Product(T.times(x.getProduct, y.getProduct))
  }
  implicit def productNumeric[A](implicit T: Numeric[A]): Numeric[Product[A]] = new Numeric[Product[A]]{
    def compare(x: Product[A], y: Product[A]): Int = T.compare(x.getProduct, y.getProduct)
    def fromInt(x: Int): Product[A] = Product(T.fromInt(x))
    def minus(x: Product[A], y: Product[A]):Product[A] = Product(T.minus(x.getProduct, y.getProduct))
    def negate(x: Product[A]): Product[A] = Product(T.negate(x.getProduct))
    def plus(x: Product[A], y: Product[A]): Product[A] = Product(T.plus(x.getProduct, y.getProduct))
    def times(x: Product[A], y: Product[A]): Product[A] = Product(T.times(x.getProduct, y.getProduct))
    def toDouble(x: Product[A]): Double = T.toDouble(x.getProduct)
    def toFloat(x: Product[A]): Float = T.toFloat(x.getProduct)
    def toInt(x: Product[A]): Int = T.toInt(x.getProduct)
    def toLong(x: Product[A]): Long  = T.toLong(x.getProduct)
  }

  implicit def productShow[A: Show]: Show[Product[A]] = 
    Show.show[Product[A]](productA => show"Product(${productA.getProduct})")

  implicit def productOrder[A: Order]: Order[Product[A]] =
    Order.by(_.getProduct)

  implicit val productInstances: Monad[Product] with NonEmptyTraverse[Product] with Distributive[Product] = 
    new Monad[Product] with NonEmptyTraverse[Product] with Distributive[Product] {
      def pure[A](a: A): Product[A] = Product(a)
      def flatMap[A,B](fa: Product[A])(f: A => Product[B]): Product[B] = f(fa.getProduct)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Product[Either[A,B]]): Product[B] =
        f(a) match {
          case Product(Left(a)) => tailRecM(a)(f)
          case Product(Right(b)) => Product(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Product[A],b: B)(f: (B, A) => B): B = 
        f(b, fa.getProduct)
      def foldRight[A, B](fa: Product[A],lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        f(fa.getProduct, lb)
      
      // Members declared in cats.NonEmptyTraverse
      def nonEmptyTraverse[G[_]: Apply, A, B](fa: Product[A])(f: A => G[B]): G[Product[B]] = 
        f(fa.getProduct).map(Product(_))
      
      // Members declared in cats.Reducible
      def reduceLeftTo[A, B](fa: Product[A])(f: A => B)(g: (B, A) => B): B = 
        f(fa.getProduct)
      def reduceRightTo[A, B](fa: Product[A])(f: A => B)(g: (A, Eval[B]) => Eval[B]): Eval[B] = 
        f(fa.getProduct).pure[Eval]
        
      def distribute[G[_]: Functor, A, B](ga: G[A])(f: A => Product[B]): Product[G[B]] = 
          Product(ga.map(f).map(_.getProduct))
    }
}

private[monoids] trait ProductInstances1 {
  implicit def ProductEq[A: Eq]: Eq[Product[A]] = Eq.by(_.getProduct)
}
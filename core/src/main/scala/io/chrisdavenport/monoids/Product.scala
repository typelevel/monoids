package io.chrisdavenport.monoids

import cats._
import cats.kernel.CommutativeMonoid
import cats.implicits._

final case class Product[A](getProduct: A)
object Product extends ProductInstances {
  def product[F[_]: Foldable, A](fa: F[A])(implicit A: Monoid[Product[A]]): A =
    fa.foldMap(Product(_)).getProduct
}

private[monoids] trait ProductInstances extends ProductInstances1 {
  implicit def productNumericMonoid[A](implicit T: Numeric[A]): CommutativeMonoid[Product[A]] =
    new CommutativeMonoid[Product[A]] {
      def empty: Product[A] = Product(T.one)
      def combine(x: Product[A], y: Product[A]): Product[A] = Product(
        T.times(x.getProduct, y.getProduct)
      )
    }

  implicit def productShow[A: Show]: Show[Product[A]] =
    Show.show[Product[A]](productA => show"Product(${productA.getProduct})")

  implicit def productOrder[A: Order]: Order[Product[A]] =
    Order.by(_.getProduct)

  implicit val productInstances: CommutativeMonad[Product]
    with NonEmptyTraverse[Product]
    with Distributive[Product] =
    new CommutativeMonad[Product] with NonEmptyTraverse[Product] with Distributive[Product] {
      def pure[A](a: A): Product[A] = Product(a)
      def flatMap[A, B](fa: Product[A])(f: A => Product[B]): Product[B] = f(fa.getProduct)

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => Product[Either[A, B]]): Product[B] =
        f(a) match {
          case Product(Left(a)) => tailRecM(a)(f)
          case Product(Right(b)) => Product(b)
        }
      // Members declared in cats.Foldable
      def foldLeft[A, B](fa: Product[A], b: B)(f: (B, A) => B): B =
        f(b, fa.getProduct)
      def foldRight[A, B](fa: Product[A], lb: cats.Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
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

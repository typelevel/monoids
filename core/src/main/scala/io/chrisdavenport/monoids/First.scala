package io.chrisdavenport.monoids

import cats._
import cats.implicits._

final case class First[A](getFirst: Option[A]) extends AnyVal
object First extends FirstInstances

private[monoids] trait FirstInstances extends FirstInstances1 {
  implicit def firstMonoid[A]: Monoid[First[A]] = MonoidK[First].algebra

  implicit def firstShow[A: Show]: Show[First[A]] =
    Show.show[First[A]](FirstA => show"First(${FirstA.getFirst})")

  implicit def firstOrder[A: Order]: Order[First[A]] =
    Order.by(_.getFirst)

  implicit val firstInstances: Monad[First] with Traverse[First] with Alternative[First] =
    new Monad[First] with Traverse[First] with Alternative[First] {
      def pure[A](a: A): First[A] = First(Some(a))
      def flatMap[A, B](fa: First[A])(f: A => First[B]): First[B] =
        First(fa.getFirst.flatMap(f.andThen(_.getFirst)))

      @scala.annotation.tailrec
      def tailRecM[A, B](a: A)(f: A => First[Either[A, B]]): First[B] =
        f(a) match {
          case First(Some(Left(a))) => tailRecM(a)(f)
          case First(Some(Right(b))) => First(b.some)
          case First(None) => First(None)
        }
      def foldLeft[A, B](fa: First[A], b: B)(f: (B, A) => B): B =
        fa.getFirst.foldLeft(b)(f)
      def foldRight[A, B](fa: First[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.getFirst.foldRight(lb)(f)

      def traverse[G[_]: Applicative, A, B](fa: First[A])(f: A => G[B]): G[First[B]] =
        fa.getFirst.traverse(f).map(First(_))

      def empty[A]: First[A] = First(Option.empty[A])

      // Members declared in cats.SemigroupK
      def combineK[A](x: First[A], y: First[A]): First[A] = First(x.getFirst.orElse(y.getFirst))

    }
}

private[monoids] trait FirstInstances1 {
  implicit def FirstEq[A: Eq]: Eq[First[A]] = Eq.by(_.getFirst)
}

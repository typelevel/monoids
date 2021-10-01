package io.chrisdavenport.monoids

opaque type Product[A] = A

object Product extends ProductInstances:
  def apply[A](getProduct: A): Product[A] = getProduct
  def unapply[A](arg: Product[A]): Option[A] = Some(arg.getProduct)

extension [A](p: Product[A])
  def getProduct: A = p

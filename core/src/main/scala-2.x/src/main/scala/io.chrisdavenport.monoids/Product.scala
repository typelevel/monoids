package io.chrisdavenport.monoids

final case class Product[A](getProduct: A) extends AnyVal

object Product extends ProductInstances

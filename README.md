# monoids ![Continuous Integration](https://github.com/typelevel/monoids/workflows/Continuous%20Integration/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/monoids_2.13)


Monoids is a library for some generic Monoids that are very useful.

This library exposes

- `All` - Boolean newtype that combines values using `&&`
- `Any` - Boolean newtype that combines values using `||`
- `Dual` - Inverts the Combine operation of a monoid
- `First` - Option newtype that combine takes the first element that is present
- `Last` - Option newtype that combine takes the last element that is present
- `Product` - Numeric newtype that combines values using Multiplication
- `Sum` - Numeric newtype that combines values using Addition

## [Head on over to the Microsite](https://typelevel.org/monoids/)

## Quick Start

To use this project in an existing SBT project with Scala 2.11 or a later version, add the following dependencies to your
`build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "io.chrisdavenport" %% "monoids" % "<version>"
)
```

# monoids [![Build Status](https://travis-ci.com/ChristopherDavenport/monoids.svg?branch=master)](https://travis-ci.com/ChristopherDavenport/monoids)

Monoids is a library for some generic Monoids that are very useful.

This library exposes

- `All` - Boolean newtype that combines values using `&&`
- `Any` - Boolean newtype that combines values using `||`
- `Dual` - Inverts the Combine operation of a monoid
- `First` - Option newtype that combine takes the first element that is present
- `Last` - Option newtype that combine takes the last element that is present
- `Product` - Numeric newtype that combines values using Multiplication
- `Sum` - Numeric newtype that combines values using Addition
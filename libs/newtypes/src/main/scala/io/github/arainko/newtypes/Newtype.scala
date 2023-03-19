package io.github.arainko.newtypes

import io.github.arainko.ducktape.Transformer

trait Newtype[A] {
  opaque type Type = A

  def apply(value: A): Type = value

  def wrapAll[F[_]](unwrapped: F[A]): F[Type] = unwrapped

  def unwrapAll[F[_]](wrapped: F[Type]): F[A] = wrapped

  extension (self: Type) {
    def value: A = self
  }

  given wrappingTransformer: Transformer[A, Type] = apply

  given uwnrappingTransformer: Transformer[Type, A] = _.value
}

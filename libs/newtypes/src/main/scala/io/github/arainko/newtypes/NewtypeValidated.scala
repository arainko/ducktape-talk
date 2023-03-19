package io.github.arainko.newtypes

import io.github.arainko.ducktape.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.*
import eu.timepit.refined.api.Validate

abstract class NewtypeValidated[A, Constraint](using Validate[A, Constraint]) {
  opaque type Type = A Refined Constraint

  protected def unsafe(value: A): Type = Refined.unsafeApply[A, Constraint](value)

  def make(value: A): Either[::[String], Type] =
    refineV[Constraint](value).left.map(err => ::(err, Nil))

  extension (self: Type) {
    def value: A = self.asInstanceOf[A]
  }

  given wrappingTransformer: Transformer.Accumulating[Either[::[String], _], A, Type] = make(_)

  given unsafeWrappingTransformer(using UnsafeTransformations): Transformer[A, Type] = unsafe

  given unwrappingTransformer: Transformer[Type, A] = _.value

}

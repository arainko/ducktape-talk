package io.github.arainko.newtypes

import io.github.arainko.ducktape.*
import eu.timepit.refined.api.Refined
import eu.timepit.refined.*
import eu.timepit.refined.api.Validate
import sourcecode.Name
import sourcecode.Enclosing
import sourcecode.FullName

abstract class NewtypeValidated[A, Constraint](using Validate[A, Constraint], NewtypePath) {
  opaque type Type = A Refined Constraint

  def unsafe(value: A)(using UnsafeTransformations): Type = Refined.unsafeApply[A, Constraint](value)

  def make(value: A): Either[List[String], Type] =
    refineV[Constraint](value).left.map(err => s"Invalid ${summon[NewtypePath].value} - $err" :: Nil)

  extension (self: Type) {
    def value: A = self.asInstanceOf[A]
  }

  given wrappingTransformer: Transformer.Fallible[Either[List[String], _], A, Type] = make(_)

  given unsafeWrappingTransformer(using UnsafeTransformations): Transformer[A, Type] = unsafe

  given unwrappingTransformer: Transformer[Type, A] = _.value

}

package io.github.arainko.newtypes

import scala.annotation.implicitNotFound

@implicitNotFound("""
Unsafe transformations are opt in. 
Provide a given instance of UnsafeTransformations in your local implicit scope with `given UnsafeTransformations = UnsafeTransformations.allow`
""")
final class UnsafeTransformations private ()

object UnsafeTransformations {
  val allow: UnsafeTransformations = new UnsafeTransformations
}

package io.github.arainko.talk

import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.boolean.And

type CoolInt = CoolInt.Type
object CoolInt extends Newtype[Int]

type CoolValidatedInt = CoolValidatedInt.Type
object CoolValidatedInt extends NewtypeValidated[Int, GreaterEqual[0] And Even]

final case class Prim(int: Int, int2: Int)
final case class New(int: CoolInt, int2: CoolInt)
final case class NewValidated(int: CoolValidatedInt, int2: CoolValidatedInt)

object Main extends App {
  // println(CoolValidatedInt.make(-1))

  val t = Transformer.Debug.showCode(summon[Transformer.Accumulating[Either[::[String], _], Prim, NewValidated]])

  println(t.transform(Prim(1, 0)))

  private given UnsafeTransformations = UnsafeTransformations.allow

  Transformer.Debug.showCode(Prim(0, 0).to[New])

  // Transformer.forProducts[Prim, New]

}

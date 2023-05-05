package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.EitherT
import cats.effect.{ IO, IOApp }
import io.github.arainko.talk.SeriousBusinessApiService
import io.github.arainko.talk.SeriousBusinessApiServiceOperation._
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2

import java.{ util => ju }

// regex for extracting a readable type:
// Kind2\[\[e, a\] =>> cats\.data\.EitherT\[cats\.effect\.IO, e, a\]\]#toKind5\[[a-zA-Z]+, ([a-zA-Z]+), ([a-zA-Z]+), [a-zA-Z]+, [a-zA-Z]+\]


object Main extends IOApp.Simple {
  // SimpleRestJsonBuilder.routes()
  def run = ???
}

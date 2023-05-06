package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.EitherT
import cats.effect.{ IO, IOApp }
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2

import java.{ util => ju }

// regex for extracting a readable type:
// Kind2\[\[e, a\] =>> cats\.data\.EitherT\[cats\.effect\.IO, e, a\]\]#toKind5\[[a-zA-Z]+, ([a-zA-Z]+), ([a-zA-Z]+), [a-zA-Z]+, [a-zA-Z]+\]


object Main extends IOApp.Simple {
  import cats.syntax.all.*

  val errStr: EitherT[IO, String, Unit] = ???

  val intErr: EitherT[IO, Int, Unit] = ???

  val union: EitherT[IO, String | Int, Unit] = errStr.flatMap(_ => intErr.leftWiden)
    // for {
    //   _ <- errStr
    //   _ <- intErr.leftWiden
    //   // _ <- errStr.leftWiden
    // } yield ()

  // SimpleRestJsonBuilder.routes()
  def run = ???
}

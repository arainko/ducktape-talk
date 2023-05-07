package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.EitherT
import cats.effect.kernel.Resource
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.{ host, port }
import io.github.arainko.talk.infrastructure.repository.ConferenceRepositoryInMemory
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.CORS
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2

import java.{ util => ju }

// regex for extracting a readable type:
// Kind2\[\[e, a\] =>> cats\.data\.EitherT\[cats\.effect\.IO, e, a\]\]#toKind5\[[a-zA-Z]+, ([a-zA-Z]+), ([a-zA-Z]+), [a-zA-Z]+, [a-zA-Z]+\]

object Main extends IOApp.Simple {
  def run =
    Resource
      .eval(ConferenceRepositoryInMemory.create)
      .flatMap(ConferenceRoutes.create)
      .map(CORS.policy.withAllowOriginAll.httpRoutes)
      .flatMap { routes =>
        EmberServerBuilder
          .default[IO]
          .withHttpApp(routes.orNotFound)
          .withHost(host"0.0.0.0")
          .withPort(port"9002")
          .build
      }
      .useForever
}

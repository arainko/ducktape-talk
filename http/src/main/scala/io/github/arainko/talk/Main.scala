package io.github.arainko.talk

import cats.effect.kernel.Resource
import cats.effect.{ IO, IOApp }
import com.comcast.ip4s.*
import io.github.arainko.talk.http.ConferenceRoutes
import io.github.arainko.talk.infrastructure.repository.ConferenceRepositoryInMemory
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.CORS

object Main extends IOApp.Simple {

  def run =
    Resource
      .eval(ConferenceRepositoryInMemory.create)
      .map(ConferenceRoutes.create)
      .map(CORS.policy.withAllowOriginAll.httpRoutes)
      .flatMap { routes =>
        EmberServerBuilder
          .default[IO]
          .withHttpApp(routes.orNotFound)
          .withHost(host"0.0.0.0")
          .withPort(port"9001")
          .build
      }
      .useForever

}

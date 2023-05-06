package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.EitherT
import cats.effect.{ IO, IOApp }
import io.github.arainko.ducktape.Transformer
import io.github.arainko.ducktape.fallible.Mode.Accumulating
import io.github.arainko.talk.API
import io.github.arainko.talk.API.SeriousBusinessApiServiceOperation.*
import io.github.arainko.talk.domain.model.*
import io.github.arainko.talk.domain.repository.ConferenceRepository
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2
import io.github.arainko.ducktape.*
import cats.syntax.all.*

import java.{ util => ju }

class ConferenceRoutes(conferenceRepo: ConferenceRepository)
    extends API.SeriousBusinessApiService.ErrorAware[[e, a] =>> EitherT[IO, e, a]] {

  override def fetchConferences(): EitherT[IO, Nothing, API.FetchConferencesOutput] =
    EitherT.liftF {
      conferenceRepo.fetchAll.map(confs =>
        API.FetchConferencesOutput(
          confs
            .map(conf =>
              conf
                .into[API.GetConference]
                .transform(
                  Field.computed(_.name, _.info.name.value),
                  Field.computed(_.city, _.info.city.value),
                  Field.computed(_.dateSpan, _.info.dateSpan.to[API.DateSpan])
                )
            )
            .toList
        )
      )
    }

  override def updateConference(
    conferenceId: ju.UUID,
    body: API.UpdateConferenceBody
  ): EitherT[IO, UpdateConferenceError, Unit] = ???

  override def createConference(
    body: API.CreateConferenceBody
  ): EitherT[IO, CreateConferenceError, API.CreateConferenceOutput] = ???

  override def deleteTalk(conferenceId: ju.UUID, talkId: ju.UUID): EitherT[IO, DeleteTalkError, Unit] = ???

  override def updateTalk(
    conferenceId: ju.UUID,
    talkId: ju.UUID,
    body: API.UpdateTalkBody
  ): EitherT[IO, UpdateTalkError, API.UpdateTalkOutput] = ???

  override def createTalk(
    conferenceId: ju.UUID,
    body: API.CreateTalkBody
  ): EitherT[IO, CreateTalkError, API.CreateTalkOutput] =
    ???

  override def deleteConference(conferenceId: ju.UUID): EitherT[IO, DeleteConferenceError, Unit] = ???

  // private given Accumulating[[A] =>> Either[List[Predef.String], A]] =
  //   Transformer.Mode.Accumulating.either[String, List]
}

package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.EitherT
import cats.effect.{ IO, IOApp }
import io.github.arainko.talk.SeriousBusinessApiService
import io.github.arainko.talk.SeriousBusinessApiServiceOperation._
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2
import java.{ util => ju }
import io.github.arainko.talk.domain.repository.ConferenceRepository
import io.github.arainko.ducktape.Transformer
import io.github.arainko.ducktape.fallible.Mode.Accumulating
import io.github.arainko.talk.domain.model.*

class ConferenceRoutes(conferenceRepo: ConferenceRepository)
    extends SeriousBusinessApiService.ErrorAware[[e, a] =>> EitherT[IO, e, a]] {

  // private given Accumulating[[A] =>> Either[List[Predef.String], A]] =
  //   Transformer.Mode.Accumulating.either[String, List]

  override def fetchConferences(): EitherT[IO, Nothing, FetchConferencesOutput] = ???

  override def createConference(
    body: CreateConferenceBody
  ): EitherT[IO, CreateConferenceError, CreateConferenceOutput] = ???

  override def deleteTalk(conferenceId: ju.UUID, talkId: ju.UUID): EitherT[IO, DeleteTalkError, Unit] =
    conferenceRepo
      .deleteTalk(Conference.Id(conferenceId), Talk.Id(talkId))
      .map {
        case Left(confNotFound: ConferenceNotFound) =>
          DeleteTalkError.ConferenceNotFoundCase(ConferenceNotFound(ErrorMessage(confNotFound.message)))
        case Left(talkNotFound: TalkNotFound) =>
          respond.NotFound(API.ErrorMessage(talkNotFound.message))
        case Right(_) =>
          respond.NoContent
      }

  override def createTalk(conferenceId: ju.UUID, body: CreateTalkBody): EitherT[IO, CreateTalkError, CreateTalkOutput] =
    ???

  override def updateTalk(
    conferenceId: ju.UUID,
    talkId: ju.UUID,
    body: UpdateTalkBody
  ): EitherT[IO, UpdateTalkError, UpdateTalkOutput] = ???

  override def deleteConference(conferenceId: ju.UUID): EitherT[IO, DeleteConferenceError, Unit] = ???

  override def updateConference(
    conferenceId: ju.UUID,
    body: UpdateConferenceBody
  ): EitherT[IO, UpdateConferenceError, Unit] = ???

}

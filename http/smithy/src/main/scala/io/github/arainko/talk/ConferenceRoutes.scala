package io.github.arainko.talk

import alloy.SimpleRestJson
import cats.data.{ EitherT, Kleisli }
import cats.effect.kernel.Resource
import cats.effect.{ IO, IOApp }
import cats.syntax.either.*
import cats.syntax.functor.*
import io.github.arainko.ducktape.fallible.Mode.Accumulating
import io.github.arainko.ducktape.{ Transformer, _ }
import io.github.arainko.talk.API
import io.github.arainko.talk.API.SeriousBusinessApiServiceOperation.*
import io.github.arainko.talk.API.ValidationErrors
import io.github.arainko.talk.domain.model.*
import io.github.arainko.talk.domain.repository.{ ConferenceNotFound, ConferenceRepository, TalkNotFound }
import org.http4s.{ HttpRoutes, Request, Response }
import smithy4s.Transformation
import smithy4s.http4s.SimpleRestJsonBuilder
import smithy4s.kinds.Kind2

import java.util.UUID
import java.{ util => ju }

class ConferenceRoutes(conferenceRepo: ConferenceRepository)
    extends API.SeriousBusinessApiService.ErrorAware[[e, a] =>> EitherT[IO, e, a]] {

  private given Accumulating[[A] =>> Either[List[Predef.String], A]] =
    Transformer.Mode.Accumulating.either[String, List]

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
  ): EitherT[IO, UpdateConferenceError, Unit] =
    body
      .into[Conference.Info]
      .fallible
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(ValidationErrors.apply)
      .toEitherT[IO]
      .semiflatMap(info =>
        conferenceRepo
          .update(Conference.Id(conferenceId), info)
          .map(_.leftMap(confNotFound => API.ConferenceNotFound(confNotFound.message)))
      )

  override def createConference(
    body: API.CreateConferenceBody
  ): EitherT[IO, CreateConferenceError, API.CreateConferenceOutput] =
    body
      .into[Conference.Info]
      .fallible
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(ValidationErrors.apply)
      .map(info => Conference(Conference.Id(UUID.randomUUID), info, Vector.empty))
      .toEitherT[IO]
      .semiflatMap(conf => conferenceRepo.create(conf).as(API.CreateConferenceOutput(API.CreatedId(conf.id.value))))

  override def deleteTalk(conferenceId: ju.UUID, talkId: ju.UUID): EitherT[IO, DeleteTalkError, Unit] =
    EitherT(conferenceRepo.deleteTalk(Conference.Id(conferenceId), Talk.Id(talkId))).leftMap {
      case confNotFound: ConferenceNotFound => API.ConferenceOrTalkNotFound(confNotFound.message)
      case talkNotFound: TalkNotFound       => API.ConferenceOrTalkNotFound(talkNotFound.message)
    }

  override def updateTalk(
    conferenceId: ju.UUID,
    talkId: ju.UUID,
    body: API.UpdateTalkBody
  ): EitherT[IO, UpdateTalkError, API.UpdateTalkOutput] =
    body
      .into[Talk]
      .fallible
      .transform(Field.const(_.id, Talk.Id(talkId)))
      .leftMap(ValidationErrors.apply)
      .toEitherT[IO]
      .flatMap(talk =>
        EitherT(conferenceRepo.updateTalk(Conference.Id(conferenceId), talk)).leftMap {
          case confNotFound: ConferenceNotFound =>
            API.ConferenceOrTalkNotFound(confNotFound.message)
          case talkNotFound: TalkNotFound =>
            API.ConferenceOrTalkNotFound(talkNotFound.message)
        }.as(API.UpdateTalkOutput(API.CreatedId(talk.id.value)))
      )

  override def createTalk(
    conferenceId: ju.UUID,
    body: API.CreateTalkBody
  ): EitherT[IO, CreateTalkError, API.CreateTalkOutput] =
    body
      .into[Talk]
      .fallible
      .transform(Field.const(_.id, Talk.Id(UUID.randomUUID)))
      .leftMap(ValidationErrors.apply)
      .toEitherT[IO]
      .flatMap(talk =>
        EitherT(conferenceRepo.createTalk(Conference.Id(conferenceId), talk))
          .leftMap(confNotFound => API.ConferenceNotFound(confNotFound.message))
          .as(API.CreateTalkOutput(API.CreatedId(talk.id.value)))
      )

  override def deleteConference(conferenceId: ju.UUID): EitherT[IO, DeleteConferenceError, Unit] =
    EitherT(conferenceRepo.delete(Conference.Id(conferenceId)))
      .leftMap(confNotFound => API.ConferenceNotFound(confNotFound.message))
}

object ConferenceRoutes {
  def create(repo: ConferenceRepository): Resource[IO, HttpRoutes[IO]] =
    SimpleRestJsonBuilder
      .routes(ConferenceRoutes(repo).transform(absorbErrors))
      .resource

  private val absorbErrors = new Transformation.AbsorbError[EitherT[IO, _, _], IO] {
    override def apply[E, A](fa: EitherT[IO, E, A], injectError: E => Throwable): IO[A] =
      fa.leftMap(injectError).rethrowT
  }
}

package io.github.arainko.talk.http

import cats.effect.IO
import cats.syntax.all.*
import io.github.arainko.ducktape.*
import io.github.arainko.talk.domain.model.*
import io.github.arainko.talk.domain.repository.*
import io.github.arainko.talk.generated.Resource.*
import io.github.arainko.talk.generated.definitions.API
import io.github.arainko.talk.generated.{ Handler, Resource }
import org.http4s.HttpRoutes
import org.http4s.server.middleware.Logger

import java.util.UUID
import java.{ util => ju }

final class ConferenceRoutes(conferenceRepo: ConferenceRepository) extends Handler[IO] {

  private given Mode.Accumulating[[A] =>> Either[List[Predef.String], A]] =
    Mode.Accumulating.either[String, List]

  override def deleteTalk(
    respond: DeleteTalkResponse.type
  )(conferenceId: ju.UUID, talkId: ju.UUID): IO[DeleteTalkResponse] =
    conferenceRepo
      .deleteTalk(Conference.Id(conferenceId), Talk.Id(talkId))
      .map {
        case Left(confNotFound: ConferenceNotFound) =>
          respond.NotFound(API.ErrorMessage(confNotFound.message))
        case Left(talkNotFound: TalkNotFound) =>
          respond.NotFound(API.ErrorMessage(talkNotFound.message))
        case Right(_) =>
          respond.NoContent
      }

  override def fetchConferences(respond: FetchConferencesResponse.type)(): IO[FetchConferencesResponse] =
    conferenceRepo.fetchAll.map(confs =>
      respond.Ok(
        confs.map(conf =>
          conf
            .into[API.GetConference]
            .transform(
              Field.computed(_.name, _.info.name.value),
              Field.computed(_.city, _.info.city.value),
              Field.computed(_.dateSpan, _.info.dateSpan.to[API.DateSpan])
            )
        )
      )
    )

  override def createConference(
    respond: CreateConferenceResponse.type
  )(body: API.CreateConference): IO[CreateConferenceResponse] =
    body
      .into[Conference.Info]
      .fallible
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(respondWithValidationErrors(respond.BadRequest.apply))
      .map(info => Conference(Conference.Id(UUID.randomUUID), info, Vector.empty))
      .toEitherT[IO]
      .semiflatMap(conf => conferenceRepo.create(conf).as(respond.Created(API.CreatedId(conf.id.value))))
      .merge

  override def updateConference(
    respond: UpdateConferenceResponse.type
  )(conferenceId: ju.UUID, body: API.UpdateConference): IO[UpdateConferenceResponse] =
    body
      .into[Conference.Info]
      .fallible
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(respondWithValidationErrors(respond.BadRequest.apply))
      .toEitherT[IO]
      .semiflatMap(info =>
        conferenceRepo
          .update(Conference.Id(conferenceId), info)
          .map {
            case Left(confNotFound) =>
              respond.NotFound(API.ErrorMessage(confNotFound.message))
            case Right(_) =>
              respond.NoContent
          }
      )
      .merge

  override def updateTalk(
    respond: UpdateTalkResponse.type
  )(conferenceId: ju.UUID, talkId: ju.UUID, body: API.UpdateTalk): IO[UpdateTalkResponse] =
    body
      .into[Talk]
      .fallible
      .transform(Field.const(_.id, Talk.Id(talkId)))
      .leftMap(respondWithValidationErrors(respond.BadRequest.apply))
      .toEitherT[IO]
      .semiflatMap(talk =>
        conferenceRepo.updateTalk(Conference.Id(conferenceId), talk).map {
          case Left(confNotFound: ConferenceNotFound) =>
            respond.NotFound(API.ErrorMessage(confNotFound.message))
          case Left(talkNotFound: TalkNotFound) =>
            respond.NotFound(API.ErrorMessage(talkNotFound.message))
          case Right(value) => respond.Created(API.CreatedId(talk.id.value))
        }
      )
      .merge

  override def deleteConference(
    respond: DeleteConferenceResponse.type
  )(conferenceId: ju.UUID): IO[DeleteConferenceResponse] =
    conferenceRepo.delete(Conference.Id(conferenceId)).map {
      case Left(confNotFound) =>
        respond.NotFound(API.ErrorMessage(confNotFound.message))
      case Right(_) => respond.NoContent
    }

  override def createTalk(
    respond: CreateTalkResponse.type
  )(conferenceId: ju.UUID, body: API.CreateTalk): IO[CreateTalkResponse] =
    body
      .into[Talk]
      .fallible
      .transform(Field.const(_.id, Talk.Id(UUID.randomUUID)))
      .leftMap(respondWithValidationErrors(respond.BadRequest.apply))
      .toEitherT[IO]
      .semiflatMap(talk =>
        conferenceRepo.createTalk(Conference.Id(conferenceId), talk).map {
          case Left(confNotFound) =>
            respond.NotFound(API.ErrorMessage(confNotFound.message))
          case Right(value) => respond.Created(API.CreatedId(talk.id.value))
        }
      )
      .merge

  private def respondWithValidationErrors[A](wrapper: API.ValidationErrors => A)(errors: List[String]) =
    wrapper(API.ValidationErrors(errors.toVector))

}

object ConferenceRoutes {
  def create(repo: ConferenceRepository): HttpRoutes[cats.effect.IO] =
    Logger.httpRoutes[IO](logHeaders = false, logBody = false) {
      Resource[IO]().routes(ConferenceRoutes(repo))
    }

}

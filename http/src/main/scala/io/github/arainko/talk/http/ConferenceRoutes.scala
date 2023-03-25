package io.github.arainko.talk.http

import io.github.arainko.talk.generated.definitions
import io.github.arainko.talk.generated.Handler
import cats.effect.IO
import io.github.arainko.talk.generated.Resource.*
import java.{ util => ju }
import io.github.arainko.talk.domain.repository.ConferenceRepository
import io.github.arainko.ducktape.*
import cats.syntax.all.*
import io.github.arainko.talk.domain.*
import io.github.arainko.talk.domain.repository.ConferenceNotFound
import io.github.arainko.talk.domain.repository.TalkNotFound
import java.util.UUID

final class ConferenceRoutes(conferenceRepo: ConferenceRepository) extends Handler[IO] {

  override def deleteTalk(
    respond: DeleteTalkResponse.type
  )(conferenceId: ju.UUID, talkId: ju.UUID): IO[DeleteTalkResponse] =
    conferenceRepo
      .deleteTalk(Conference.Id(conferenceId), Talk.Id(talkId))
      .map {
        case Left(confNotFound: ConferenceNotFound) =>
          respond.NotFound(definitions.ErrorMessage(s"Conference with id: $conferenceId not found"))
        case Left(talkNotFound: TalkNotFound) =>
          respond.NotFound(
            definitions.ErrorMessage(s"Talk with id: $talkId not found in conference with id: $conferenceId")
          )
        case Right(_) =>
          respond.NoContent
      }

  override def fetchConferences(respond: FetchConferencesResponse.type)(): IO[FetchConferencesResponse] =
    conferenceRepo.fetchAll.map(confs =>
      respond.Ok(
        confs.map(conf =>
          conf
            .into[definitions.GetConference]
            .transform(
              Field.computed(_.name, _.info.name.value),
              Field.computed(_.city, _.info.city.value),
              Field.computed(_.dateSpan, _.info.dateSpan.to[definitions.DateSpan])
            )
        )
      )
    )

  override def createConference(
    respond: CreateConferenceResponse.type
  )(body: definitions.CreateConference): IO[CreateConferenceResponse] =
    body
      .into[Conference.Info]
      .accumulating[Either[::[String], _]]
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(errors => respond.BadRequest(definitions.ValidationErrors(errors.toVector)))
      .map(info => Conference(Conference.Id(UUID.randomUUID), info, Vector.empty))
      .toEitherT[IO]
      .semiflatMap(conf => conferenceRepo.create(conf).as(respond.Created(definitions.CreatedId(conf.id.value))))
      .merge

  override def updateConference(
    respond: UpdateConferenceResponse.type
  )(conferenceId: ju.UUID, body: definitions.UpdateConference): IO[UpdateConferenceResponse] =
    body
      .into[Conference.Info]
      .accumulating[Either[::[String], _]]
      .transform(Field.fallibleComputed(_.dateSpan, _.dateSpan.via(DateSpan.create)))
      .leftMap(errors => respond.BadRequest(definitions.ValidationErrors(errors.toVector)))
      .toEitherT[IO]
      .semiflatMap(info =>
        conferenceRepo
          .update(Conference.Id(conferenceId), info)
          .map {
            case Left(confNotFound) =>
              respond.NotFound(definitions.ErrorMessage(s"Conference with id: $conferenceId not found"))
            case Right(value) =>
              respond.NoContent
          }
      )
      .merge

  override def updateTalk(
    respond: UpdateTalkResponse.type
  )(conferenceId: ju.UUID, talkId: ju.UUID, body: definitions.UpdateTalk): IO[UpdateTalkResponse] =
    body
      .into[Talk]
      .accumulating[Either[::[String], _]]
      .transform(Field.const(_.id, Talk.Id(talkId)))
      .leftMap(errors => respond.BadRequest(definitions.ValidationErrors(errors.toVector)))
      .toEitherT[IO]
      .semiflatMap(talk =>
        conferenceRepo.updateTalk(Conference.Id(conferenceId), talk).map {
          case Left(confNotFound: ConferenceNotFound) =>
            respond.NotFound(definitions.ErrorMessage(s"Conference with id: $conferenceId not found"))
          case Left(talkNotFound: TalkNotFound) =>
            respond.NotFound(
              definitions.ErrorMessage(s"Talk with id: $talkId not found in conference with id: $conferenceId")
            )
          case Right(value) => respond.Created(definitions.CreatedId(talk.id.value))
        }
      )
      .merge

  override def deleteConference(
    respond: DeleteConferenceResponse.type
  )(conferenceId: ju.UUID): IO[DeleteConferenceResponse] =
    conferenceRepo.delete(Conference.Id(conferenceId)).map {
      case Left(confNotFound) =>
        respond.NotFound(definitions.ErrorMessage(s"Conference with id: $conferenceId not found"))
      case Right(_) => respond.NoContent
    }

  override def createTalk(
    respond: CreateTalkResponse.type
  )(conferenceId: ju.UUID, body: definitions.CreateTalk): IO[CreateTalkResponse] =
    body
      .into[Talk]
      .accumulating[Either[::[String], _]]
      .transform(Field.const(_.id, Talk.Id(UUID.randomUUID)))
      .leftMap(errors => respond.BadRequest(definitions.ValidationErrors(errors.toVector)))
      .toEitherT[IO]
      .semiflatMap(talk =>
        conferenceRepo.createTalk(Conference.Id(conferenceId), talk).map {
          case Left(confNotFound) =>
            respond.NotFound(definitions.ErrorMessage(s"Conference with id: $conferenceId not found"))
          case Right(value) => respond.Created(definitions.CreatedId(talk.id.value))
        }
      )
      .merge

}

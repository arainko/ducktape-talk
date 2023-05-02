package io.github.arainko.talk.infrastructure.repository

import cats.effect.IO
import cats.effect.std.AtomicCell
import cats.syntax.all.*
import io.github.arainko.newtypes.UnsafeTransformations
import io.github.arainko.talk.domain.model.*
import io.github.arainko.talk.domain.repository.*
import io.github.arainko.talk.infrastructure.model as storageModel
import monocle.*

import java.util.UUID
import monocle.function.At

class ConferenceRepositoryInMemory private (storage: AtomicCell[IO, Map[UUID, storageModel.Conference]])
    extends ConferenceRepository {

  private given UnsafeTransformations = UnsafeTransformations.allow

  private val confs = Iso.id[Map[UUID, storageModel.Conference]]
  private val talks = Focus[storageModel.Conference](_.talks)
  private val info  = Focus[storageModel.Conference](_.info)

  override def createTalk(conferenceId: Conference.Id, talk: Talk): IO[Either[ConferenceNotFound, Unit]] =
    storage.evalUpdate { current =>
      confs
        .at(conferenceId.value)
        .some
        .andThen(talks)
        .at(talk.id.value)
        .replaceOption(Some(storageModel.Talk.fromDomain(talk)))(current)
        .liftTo[IO](ConferenceNotFound(conferenceId))
    }.void.attemptNarrow[ConferenceNotFound]

  override def update(id: Conference.Id, info: Conference.Info): IO[Either[ConferenceNotFound, Unit]] =
    storage.evalUpdate { current =>
      confs
        .at(id.value)
        .some
        .andThen(this.info)
        .replaceOption(storageModel.Conference.Info.fromDomain(info))(current)
        .liftTo[IO](ConferenceNotFound(id))
    }.void.attemptNarrow[ConferenceNotFound]

  override def create(conference: Conference): IO[Unit] =
    storage.update(_ + (conference.id.value -> storageModel.Conference.fromDomain(conference)))

  override def updateTalk(
    conferenceId: Conference.Id,
    talk: Talk
  ): IO[Either[ConferenceNotFound | TalkNotFound, Unit]] =
    storage.evalUpdate { current =>
      current
        .get(conferenceId.value)
        .liftTo[IO](ConferenceNotFound(conferenceId))
        .productR {
          confs
            .at(conferenceId.value)
            .some
            .andThen(talks)
            .at(talk.id.value)
            .some
            .replaceOption(storageModel.Talk.fromDomain(talk))(current)
            .liftTo[IO](TalkNotFound(conferenceId, talk.id))
        }
    }.map(_.asRight).recover {
      case conf: ConferenceNotFound => Left(conf)
      case talk: TalkNotFound       => Left(talk)
    }

  override def delete(id: Conference.Id): IO[Either[ConferenceNotFound, Unit]] =
    storage.evalUpdate { current =>
      current
        .get(id.value)
        .liftTo[IO](ConferenceNotFound(id))
        .as(confs.at(id.value).replace(None)(current))
    }.attemptNarrow[ConferenceNotFound]

  override def deleteTalk(
    conferenceId: Conference.Id,
    talkId: Talk.Id
  ): IO[Either[ConferenceNotFound | TalkNotFound, Unit]] =
    storage.evalUpdate { current =>
      current
        .get(conferenceId.value)
        .liftTo[IO](ConferenceNotFound(conferenceId))
        .flatMap(_.talks.get(talkId.value).liftTo[IO](TalkNotFound(conferenceId, talkId)))
        .as {
          confs
            .at(conferenceId.value)
            .some
            .andThen(talks)
            .at(talkId.value)
            .replace(None)(current)
        }
    }.map(_.asRight).recover {
      case conf: ConferenceNotFound => Left(conf)
      case talk: TalkNotFound       => Left(talk)
    }

  override def fetchAll: IO[Vector[Conference]] =
    storage.get.map(_.values.toVector.map(_.toDomain))

}

object ConferenceRepositoryInMemory {
  val create: IO[ConferenceRepositoryInMemory] =
    AtomicCell[IO]
      .of(Map.empty[UUID, storageModel.Conference])
      .map(ConferenceRepositoryInMemory(_))
}

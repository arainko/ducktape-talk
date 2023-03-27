package io.github.arainko.talk.infrastructure.repository

import io.github.arainko.talk.domain.repository.*
import io.github.arainko.talk.domain.model.*
import cats.effect.IO
import cats.effect.std.AtomicCell
import monocle.*
import monocle.syntax.all.*
import java.util.UUID
import io.github.arainko.talk.infrastructure.model as storageModel
import io.github.arainko.newtypes.UnsafeTransformations
import io.github.arainko.ducktape.*
import cats.syntax.all.*

class ConferenceRepositoryInMemory private (storage: AtomicCell[IO, Map[UUID, storageModel.Conference]])
    extends ConferenceRepository {

  private given UnsafeTransformations = UnsafeTransformations.allow

  private val confs = Iso.id[Map[UUID, storageModel.Conference]]
  private val talks = Focus[storageModel.Conference](_.talks)

  override def createTalk(conferenceId: Conference.Id, talk: Talk): IO[Either[ConferenceNotFound, Unit]] =
    confs.at(conferenceId.value).andThen(talks.asOptional)

  override def update(id: Conference.Id, info: Conference.Info): IO[Either[ConferenceNotFound, Unit]] = ???

  override def create(conference: Conference): IO[Unit] =
    storage.update(_ + (conference.id.value -> conference.to[storageModel.Conference]))

  override def updateTalk(
    conferenceId: Conference.Id,
    talk: Talk
  ): IO[Either[ConferenceNotFound | TalkNotFound, Unit]] = ???

  override def delete(id: Conference.Id): IO[Either[ConferenceNotFound, Unit]] =
    storage
      .evalUpdate(current => IO.fromOption(confs.at(id.value).replaceOption(None)(current))(ConferenceNotFound(id)))
      .attemptNarrow[ConferenceNotFound]

  override def deleteTalk(
    conferenceId: Conference.Id,
    talkId: Talk.Id
  ): IO[Either[ConferenceNotFound | TalkNotFound, Unit]] = ???

  override def fetchAll: IO[Vector[Conference]] =
    storage.get.map(_.values.toVector.map(_.to[Conference]))

}

object ConferenceRepositoryInMemory {}

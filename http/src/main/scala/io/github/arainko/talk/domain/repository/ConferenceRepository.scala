package io.github.arainko.talk.domain.repository

import io.github.arainko.talk.domain.Conference
import cats.effect.IO
import io.github.arainko.talk.domain.Talk

trait ConferenceRepository {
  def fetchAll: IO[Vector[Conference]]
  
  def create(conference: Conference): IO[Unit]
  
  def update(id: Conference.Id, info: Conference.Info): IO[Either[ConferenceNotFound, Unit]]
  
  def delete(id: Conference.Id): IO[Either[ConferenceNotFound, Unit]]

  def updateTalk(conferenceId: Conference.Id, talk: Talk): IO[Either[ConferenceNotFound | TalkNotFound, Unit]]
  
  def deleteTalk(conferenceId: Conference.Id, talkId: Talk.Id): IO[Either[ConferenceNotFound | TalkNotFound, Unit]]

  def createTalk(conferenceId: Conference.Id, talk: Talk): IO[Either[ConferenceNotFound, Unit]]
}

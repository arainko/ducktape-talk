package io.github.arainko.talk.domain.repository

import io.github.arainko.talk.domain.model.Conference
import scala.util.control.NoStackTrace

final case class ConferenceNotFound(id: Conference.Id) extends NoStackTrace {
  val message = s"Conference with id $id was not found"
}

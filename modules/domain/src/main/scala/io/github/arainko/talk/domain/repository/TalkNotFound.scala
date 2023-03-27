package io.github.arainko.talk.domain.repository

import scala.util.control.NoStackTrace
import io.github.arainko.talk.domain.model.*

final case class TalkNotFound(conferenceId: Conference.Id, id: Talk.Id) extends NoStackTrace {
  val message = s"Talk with id $id not found in conference with id $conferenceId"
}

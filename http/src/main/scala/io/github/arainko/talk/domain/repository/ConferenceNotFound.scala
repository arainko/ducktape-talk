package io.github.arainko.talk.domain.repository

import io.github.arainko.talk.domain.Conference
import scala.util.control.NoStackTrace

final case class ConferenceNotFound(id: Conference.Id) extends NoStackTrace

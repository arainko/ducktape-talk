package io.github.arainko.talk.domain.repository

import scala.util.control.NoStackTrace
import io.github.arainko.talk.domain.Talk

final case class TalkNotFound(id: Talk.Id) extends NoStackTrace

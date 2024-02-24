package io.github.arainko.talk.infrastructure.model

import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.UnsafeTransformations
import io.github.arainko.talk.domain.model

final case class Presenter(name: String, bio: String, pronouns: Option[String])

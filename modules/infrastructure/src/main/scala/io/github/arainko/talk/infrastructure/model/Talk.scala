package io.github.arainko.talk.infrastructure.model

import java.util.UUID
import io.github.arainko.ducktape.*
import io.github.arainko.talk.domain.model
import io.github.arainko.newtypes.UnsafeTransformations

final case class Talk(id: UUID, name: String, elevatorPitch: String, presenter: Presenter)

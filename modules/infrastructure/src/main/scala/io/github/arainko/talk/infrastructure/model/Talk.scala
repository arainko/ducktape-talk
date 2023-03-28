package io.github.arainko.talk.infrastructure.model

import java.util.UUID
import io.github.arainko.ducktape.*
import io.github.arainko.talk.domain.model
import io.github.arainko.newtypes.UnsafeTransformations

final case class Talk(id: UUID, name: String, elevatorPitch: String, presenter: Presenter) {
  def toDomain(using UnsafeTransformations): model.Talk = this.to[model.Talk]
}

object Talk {
  def fromDomain(talk: model.Talk): Talk = talk.to[Talk]
}

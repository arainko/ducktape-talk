package io.github.arainko.talk.infrastructure.model

import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.UnsafeTransformations
import io.github.arainko.talk.domain.model
import io.github.arainko.talk.domain.model.DateSpan

import java.time.LocalDate
import java.util.UUID

final case class Conference(id: UUID, info: Conference.Info, talks: Map[UUID, Talk]) {
  def toDomain(using UnsafeTransformations): model.Conference =
    this
      .into[model.Conference]
      .transform(
        Field.computed(_.info, _.info.toDomain),
        Field.computed(_.talks, _.talks.values.map(_.toDomain).toVector)
      )
}

object Conference {
  def fromDomain(conf: model.Conference): Conference =
    Conference(conf.id.value, Info.fromDomain(conf.info), conf.talks.map(t => t.id.value -> Talk.fromDomain(t)).toMap)

  final case class Info(name: String, start: LocalDate, end: LocalDate, city: String) {
    def toDomain(using UnsafeTransformations): model.Conference.Info =
      this
        .into[model.Conference.Info]
        .transform(Field.computed(_.dateSpan, _.via(DateSpan.unsafe)))
  }

  object Info {
    def fromDomain(info: model.Conference.Info): Info =
      info
        .into[Conference.Info]
        .transform(Field.allMatching(info.dateSpan))
  }
}

package io.github.arainko.talk.infrastructure.model

import java.util.UUID
import java.time.LocalDate
import io.github.arainko.ducktape.*
import io.github.arainko.talk.domain.model
import io.github.arainko.newtypes.UnsafeTransformations

final case class Conference(id: UUID, info: Conference.Info, talks: Vector[Talk])

object Conference {
  final case class Info(name: String, start: LocalDate, end: LocalDate, city: String)

  object Info {
    given fromDomain: Transformer[model.Conference.Info, Conference.Info] = info =>
      info.into[Conference.Info].transform(Field.allMatching(info.dateSpan))

    given toDomain(using UnsafeTransformations): Transformer[Conference.Info, model.Conference.Info] = info =>
      info.into[model.Conference.Info].transform(Field.computed(_.dateSpan, _.via(model.DateSpan.unsafe)))
  }
}

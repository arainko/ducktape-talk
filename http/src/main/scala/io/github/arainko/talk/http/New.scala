package io.github.arainko.talk.http

import io.github.arainko.ducktape.*
import io.github.arainko.newtypes.*
import eu.timepit.refined.numeric.*
import eu.timepit.refined.boolean.And
import io.github.arainko.talk.*
import io.github.arainko.talk.domain.Conference

final case class New(int: CoolInt, int2: CoolInt)
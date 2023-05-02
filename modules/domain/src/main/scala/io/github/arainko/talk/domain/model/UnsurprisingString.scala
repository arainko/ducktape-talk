package io.github.arainko.talk.domain.model

import eu.timepit.refined.collection.*
import eu.timepit.refined.string.Trimmed
import eu.timepit.refined.boolean.And

type UnsurprisingString[Size <: Int] = Trimmed And NonEmpty And MaxSize[Size]
package io.github.arainko.newtypes

import sourcecode.FullName

final class NewtypePath private (val value: String)

object NewtypePath {

  given derived(using fullName: FullName): NewtypePath =
    NewtypePath(
      fullName.value
        .split('.')
        .dropWhile(str => !str.headOption.exists(_.isUpper) || str.contains("$package"))
        .mkString(".")
    )

}

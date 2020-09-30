package ru.otus.sc.user.model

import java.util.UUID

sealed trait Role
object Role {
  case object Reader   extends Role
  case object Manager  extends Role
  case object Manager2 extends Role
  case object Admin    extends Role
}

case class StrictUser(
    id: Option[UUID],
    firstName: String,
    lastName: String,
    age: Int,
    roles: Set[Role]
) {
  def withUpdate(upd: UpdateUser): StrictUser = {
    this.copy(
      id = Some(upd.id.getOrElse(this.id.get)),
      firstName = upd.firstName.getOrElse(this.firstName),
      lastName = upd.lastName.getOrElse(this.lastName),
      age = upd.age.getOrElse(this.age),
      roles = if (upd.roles.nonEmpty) upd.roles.get else this.roles
    )
  }
}

case class UpdateUser(
    id: Option[UUID],
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    age: Option[Int] = None,
    roles: Option[Set[Role]] = None
)

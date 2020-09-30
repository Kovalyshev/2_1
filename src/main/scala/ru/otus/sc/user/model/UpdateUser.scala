package ru.otus.sc.user.model

import java.util.UUID

object UpdateUserRequest {}

case class UpdateUserRequest(user: UpdateUser)

sealed trait UpdateUserResponse
object UpdateUserResponse {
  final case class Updated(user: StrictUser) extends UpdateUserResponse
  final case class NotFound(userId: UUID)    extends UpdateUserResponse
  final case object CantUpdateUserWithoutId  extends UpdateUserResponse
}

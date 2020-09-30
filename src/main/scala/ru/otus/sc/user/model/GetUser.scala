package ru.otus.sc.user.model

import java.util.UUID

case class GetUserRequest(userId: UUID)

sealed trait GetUserResponse
object GetUserResponse {
  case class Found(user: StrictUser) extends GetUserResponse
  case class NotFound(userId: UUID)  extends GetUserResponse
}

case class GetAllUsersResponse(users: List[StrictUser])

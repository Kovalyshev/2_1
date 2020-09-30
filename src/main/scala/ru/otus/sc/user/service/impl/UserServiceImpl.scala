package ru.otus.sc.user.service.impl

import ru.otus.sc.user.dao.UserDao
import ru.otus.sc.user.model.{
  CreateUserRequest,
  CreateUserResponse,
  DeleteUserRequest,
  DeleteUserResponse,
  FindUsersRequest,
  FindUsersResponse,
  GetAllUsersResponse,
  GetUserRequest,
  GetUserResponse,
  UpdateUserRequest,
  UpdateUserResponse
}
import ru.otus.sc.user.service.UserService

class UserServiceImpl(dao: UserDao) extends UserService {
  def createUser(request: CreateUserRequest): CreateUserResponse =
    CreateUserResponse(dao.createUser(request.user))

  def getUser(request: GetUserRequest): GetUserResponse =
    dao.getUser(request.userId) match {
      case Some(user) => GetUserResponse.Found(user)
      case None       => GetUserResponse.NotFound(request.userId)
    }

  def getAllUsers(): GetAllUsersResponse = {
    GetAllUsersResponse(
      dao
        .findAll()
        .toList
    )
  }

  def updateUser(request: UpdateUserRequest): UpdateUserResponse = {
    println(request)
    request.user.id match {
      case None => UpdateUserResponse.CantUpdateUserWithoutId
      case Some(userId) =>
        println(dao.getUser(request.user.id.get))
        dao.updateUserUpd(request.user) match {
          case Some(user) => UpdateUserResponse.Updated(user)
          case None       => UpdateUserResponse.NotFound(userId)
        }
    }
  }

  def deleteUser(request: DeleteUserRequest): DeleteUserResponse =
    dao
      .deleteUser(request.userId)
      .map(DeleteUserResponse.Deleted)
      .getOrElse(DeleteUserResponse.NotFound(request.userId))

  def findUsers(request: FindUsersRequest): FindUsersResponse =
    request match {
      case FindUsersRequest.ByLastName(lastName) =>
        FindUsersResponse.Result(dao.findByLastName(lastName))
    }
}

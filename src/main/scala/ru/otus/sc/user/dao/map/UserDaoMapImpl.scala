package ru.otus.sc.user.dao.map

import java.util.UUID

import ru.otus.sc.user.dao.UserDao
import ru.otus.sc.user.model.{StrictUser, UpdateUser}

class UserDaoMapImpl extends UserDao {
  private var users: Map[UUID, StrictUser] = Map.empty

  def createUser(user: StrictUser): StrictUser = {
    val id         = UUID.randomUUID()
    val userWithId = user.copy(id = Some(id))
    users += (id -> userWithId)
    userWithId
  }

  def getUser(userId: UUID): Option[StrictUser] = users.get(userId)

  def updateUser(user: StrictUser): Option[StrictUser] =
    for {
      id <- user.id
      _  <- users.get(id)
    } yield {
      users += (id -> user)
      user
    }

  def updateUserUpd(user: UpdateUser): Option[StrictUser] =
    for {
      id  <- user.id
      old <- users.get(id)
      upd = old.withUpdate(user)
    } yield {
      users += (id -> upd)
      upd
    }

  def deleteUser(userId: UUID): Option[StrictUser] =
    users.get(userId) match {
      case Some(user) =>
        users -= userId
        Some(user)
      case None => None
    }

  def findByLastName(lastName: String): Seq[StrictUser] =
    users.values.filter(_.lastName == lastName).toVector

  def findAll(): Seq[StrictUser] = users.values.toVector
}

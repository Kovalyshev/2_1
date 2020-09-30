package ru.otus.sc.user.dao

import java.util.UUID

import ru.otus.sc.user.model.{StrictUser, UpdateUser}

trait UserDao {
  def createUser(user: StrictUser): StrictUser
  def getUser(userId: UUID): Option[StrictUser]
  def updateUser(user: StrictUser): Option[StrictUser]
  def updateUserUpd(user: UpdateUser): Option[StrictUser]
  def deleteUser(userId: UUID): Option[StrictUser]
  def findByLastName(lastName: String): Seq[StrictUser]
  def findAll(): Seq[StrictUser]
}

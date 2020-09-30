package ru.otus.sc.user.service.impl

import java.util.UUID

import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers._
import ru.otus.sc.user.dao.UserDao
import ru.otus.sc.user.model._

class UserServiceImplTest extends AnyFreeSpec with MockFactory {

  val user1 =
    StrictUser(Some(UUID.randomUUID()), "SomeName1", "SomeLastName1", 1, Set(Role.Manager))
  val user2 = StrictUser(Some(UUID.randomUUID()), "SomeName2", "SomeLastName2", 2, Set(Role.Admin))

  val updUser1 = UpdateUser(
    Some(UUID.randomUUID()),
    Some("SomeName1"),
    Some("SomeLastName1"),
    Some(1),
    Some(Set(Role.Manager))
  )
  val updUser2 = UpdateUser(
    Some(UUID.randomUUID()),
    Some("SomeName2"),
    Some("SomeLastName2"),
    Some(2),
    Some(Set(Role.Admin))
  )

  "UserServiceTest tests" - {
    "createUser" - {
      "should create user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)

        (dao.createUser _).expects(user1).returns(user2)

        srv.createUser(CreateUserRequest(user1)) shouldBe CreateUserResponse(user2)
      }
    }

    "getUser" - {
      "should return user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)
        val id  = UUID.randomUUID()

        (dao.getUser _).expects(id).returns(Some(user1))

        srv.getUser(GetUserRequest(id)) shouldBe GetUserResponse.Found(user1)
      }

      "should return NotFound on unknown user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)
        val id  = UUID.randomUUID()

        (dao.getUser _).expects(id).returns(None)

        srv.getUser(GetUserRequest(id)) shouldBe GetUserResponse.NotFound(id)
      }
    }

    "updateUser" - {
      "should update existing user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)

        (dao.updateUserUpd _).expects(updUser1).returns(Some(user2))

        srv.updateUser(UpdateUserRequest(updUser1)) shouldBe UpdateUserResponse.Updated(user2)
      }

      "should return NotFound on unknown user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)

        (dao.updateUserUpd _).expects(updUser1).returns(None)

        srv.updateUser(UpdateUserRequest(updUser1)) shouldBe UpdateUserResponse.NotFound(
          user1.id.get
        )
      }

      "should return CantUpdateUserWithoutId on user without id" in {
        val dao  = mock[UserDao]
        val srv  = new UserServiceImpl(dao)
        val user = updUser1.copy(id = None)

        srv.updateUser(UpdateUserRequest(user)) shouldBe UpdateUserResponse.CantUpdateUserWithoutId
      }
    }

    "deleteUser" - {
      "should delete user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)
        val id  = UUID.randomUUID()

        (dao.deleteUser _).expects(id).returns(Some(user1))

        srv.deleteUser(DeleteUserRequest(id)) shouldBe DeleteUserResponse.Deleted(user1)
      }

      "should return NotFound on unknown user" in {
        val dao = mock[UserDao]
        val srv = new UserServiceImpl(dao)
        val id  = UUID.randomUUID()

        (dao.deleteUser _).expects(id).returns(None)

        srv.deleteUser(DeleteUserRequest(id)) shouldBe DeleteUserResponse.NotFound(id)
      }
    }

    "findUsers" - {
      "by last name" - {
        "should return empty list" in {
          val dao      = mock[UserDao]
          val srv      = new UserServiceImpl(dao)
          val lastName = "abc"

          (dao.findByLastName _).expects(lastName).returns(Seq.empty)

          srv.findUsers(FindUsersRequest.ByLastName(lastName)) shouldBe FindUsersResponse.Result(
            Seq.empty
          )
        }

        "should return non-empty list" in {
          val dao      = mock[UserDao]
          val srv      = new UserServiceImpl(dao)
          val lastName = "abc"

          (dao.findByLastName _).expects(lastName).returns(Seq(user1, user2))

          srv.findUsers(FindUsersRequest.ByLastName(lastName)) shouldBe FindUsersResponse.Result(
            Seq(user1, user2)
          )
        }
      }
    }
  }
}

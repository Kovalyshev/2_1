package ru.otus.sc.user.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatcher1, Route}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import ru.otus.sc.route.BaseRouter
import ru.otus.sc.user.json.UserJsonProtocol._
import ru.otus.sc.user.model._
import ru.otus.sc.user.service.UserService

class UserRouter(service: UserService) extends BaseRouter {
  def route: Route =
    pathPrefix("user") {
      getUser ~ getAllUsers ~ updateUser ~ createUser
    }

  private val UserIdRequest: PathMatcher1[GetUserRequest] = JavaUUID.map(GetUserRequest)

  private def getUser: Route =
    (get & path(UserIdRequest)) { userIdRequest =>
      service.getUser(userIdRequest) match {
        case GetUserResponse.Found(user) =>
          complete(user)
        case GetUserResponse.NotFound(_) =>
          complete(StatusCodes.NotFound)
      }
    }

  /*
   * CRUD:
   *
   * POST - without ID
   * PUT - with ID
   *
   * /users - add new - POST
   * /users/{ID} - add/update - PUT
   * Create - /users - POST
   * Read - /users GET - all
   * Read - /users/{ID} GET - one
   * Update /users/{ID} PUT
   * Delete - DELETE
   *
   *
   * */

  private def createUser: Route =
    (post & entity(as[StrictUser])) { user =>
      val response = service.createUser(CreateUserRequest(user))
      complete(response.user)
    }

  private def getAllUsers: Route =
    get {
      val response = service.getAllUsers()
      complete(response.users)
    }

  private val userId = Segment

  private def updateUser: Route =
    (put & path(JavaUUID) & entity(as[UpdateUser])) { (userId, userUpd) =>
      complete {
        // сюда не заходит
        StatusCodes.BadRequest
        //service.updateUser(UpdateUserRequest(userUpd.copy(id = Some(userId))))
      }
    }

  private def updateUser2: Route =
    put {
      // сюда заходит
      println("get put")
      path(JavaUUID) { userId =>
        // сюда уже нет
        println(s"get user id $userId")
        entity(as[UpdateUser]) { userUpd =>
          println(s"get user upd $userUpd")
          complete {
            StatusCodes.BadRequest
          }
        }
      }
    }
}

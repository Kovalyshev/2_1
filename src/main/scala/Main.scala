import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import ru.otus.sc.greet.dao.impl.GreetingDaoImpl
import ru.otus.sc.greet.route.GreetRouter
import ru.otus.sc.greet.service.impl.GreetingServiceImpl
import ru.otus.sc.user.dao.map.UserDaoMapImpl
import ru.otus.sc.user.route.UserRouter
import ru.otus.sc.user.service.impl.UserServiceImpl

import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")

    import system.dispatcher

    val greetRouter = new GreetRouter(new GreetingServiceImpl(new GreetingDaoImpl))

    val userDao     = new UserDaoMapImpl
    val userService = new UserServiceImpl(userDao)

    val userRouter = new UserRouter(userService)

    val route = greetRouter.route ~ userRouter.route

    val binding = Http().newServerAt("localhost", 8080).bind(route)

    binding.foreach(b => println(s"Binding on ${b.localAddress}"))

    StdIn.readLine()

    binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

package tunihack.cognira.services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.util.{Failure, Success}
import tunihack.cognira.routes.TeacherRoute

object Server extends App {
  val host = "0.0.0.0"
  val port = 9001

  implicit val system: ActorSystem = ActorSystem("tunihack")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  val routes = TeacherRoute.routes


  val binding = Http().bindAndHandle(routes, host, port)

  binding.onComplete {
    case Success(_)     => println(s"Server listening at $host:$port")
    case Failure(error) => println(s"Failed: ${error.getMessage}")
  }
}

package tunihack.cognira.utils

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes._
import scala.concurrent.Future
import scala.util.{Failure, Success}

object AsyncHTTP extends Config {

  implicit val system = ActorSystem("tunihack")

  /**
    * Asynchronously complete the HTTP request
    * @param f
    * @param m
    * @tparam T
    * @return RequestContext => Future[RouteResult]
    */
  def asyncComplete[T](f: Future[T])(
    implicit m: ToResponseMarshaller[T]
  ): RequestContext => Future[RouteResult] = {
    onComplete(f) {
      case Success(result) => complete(result)
      case Failure(e) =>
        complete(
          (InternalServerError, s"Something went wrong: ${e.getMessage}")
        )
    }
  }

}

package tunihack.cognira.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import tunihack.cognira.services.StudentService._
import tunihack.cognira.utils.AsyncHTTP._
import tunihack.cognira.utils.Marshaller

object StudentRoute extends Marshaller {

  def getStudentsRoute: Route = get {
    asyncComplete(getStudents)
  }

  def routes: Route = pathPrefix("students") {
    getStudentsRoute
  }
}
package tunihack.cognira.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import tunihack.cognira.services.TeacherService._
import tunihack.cognira.utils.AsyncHTTP._
import tunihack.cognira.utils.Marshaller

object TeacherRoute extends Marshaller {

  def getTeachersRoute: Route = get {
    asyncComplete(getTeachers)
  }

  def routes: Route = pathPrefix("teachers") {
    getTeachersRoute
  }
}
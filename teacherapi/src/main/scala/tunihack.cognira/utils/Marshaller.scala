package tunihack.cognira.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
import tunihack.cognira.models._

trait Marshaller
  extends SprayJsonSupport
    with DefaultJsonProtocol
    with NullOptions {
  implicit val teacherFormat = jsonFormat5(Teacher.apply)

}

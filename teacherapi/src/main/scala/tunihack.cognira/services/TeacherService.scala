package tunihack.cognira.services

import com.datastax.driver.core.Row
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.Future
import monix.reactive.Observable
import tunihack.cognira.models._
import tunihack.cognira.utils.AsyncCQL._

object TeacherService {

  /**
    * Return list of teachers from database
    * @return
    */
  def getTeachers: Future[List[Teacher]] = {
    val obs_rows: Observable[Row] = query(cql"SELECT * FROM teacher")
    futureList(obs_rows, Teacher.parseEntity)
  }

}
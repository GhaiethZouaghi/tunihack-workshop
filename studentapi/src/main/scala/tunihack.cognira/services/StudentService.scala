package tunihack.cognira.services

import com.datastax.driver.core.Row
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.Future
import monix.reactive.Observable
import tunihack.cognira.models._
import tunihack.cognira.utils.AsyncCQL._

object StudentService {

  /**
    * Return list of students from database
    * @return
    */
  def getStudents: Future[List[Student]] = {
    val obs_rows: Observable[Row] = query(cql"SELECT * FROM student")
    futureList(obs_rows, Student.parseEntity)
  }

}
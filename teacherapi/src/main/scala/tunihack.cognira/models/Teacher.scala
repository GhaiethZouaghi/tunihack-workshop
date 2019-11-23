package tunihack.cognira.models

import com.datastax.driver.core.Row
import collection.JavaConverters._

case class Teacher(id: Int,
    name: String, 
    age: Int, 
    `class`: String,
    students: List[Int])

object Teacher {
    
    def parseEntity(row: Row): Teacher = {
        Teacher(
            row.getInt("id"),
            row.getString("name"),
            row.getInt("age"),
            row.getString("class"),
            row.getList("students", classOf[java.lang.Integer]).asScala.toList.map(_.toInt)
        )
    }
}
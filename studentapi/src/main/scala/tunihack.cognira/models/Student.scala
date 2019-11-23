package tunihack.cognira.models

import com.datastax.driver.core.Row
import collection.JavaConverters._

case class Student(id: Int,
    name: String, 
    age: Int, 
    specialization: String,
    `class`: List[String])

object Student {
    
    def parseEntity(row: Row): Student = {
        Student(
            row.getInt("id"),
            row.getString("name"),
            row.getInt("age"),
            row.getString("specialization"),
            row.getList("class", classOf[String]).asScala.toList
        )
    }
}
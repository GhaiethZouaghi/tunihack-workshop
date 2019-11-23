package tunihack.cognira.persistence

import com.datastax.driver.core._
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec

import tunihack.cognira.utils.Config

/**
  * Provides a Cassandra session to be used in the system
  */
object CassandraSessionProvider extends Config {
  private var _session: Session = _

  def setSession(s: Session): Unit = { _session = s }

  def cassandraConn: Session = {
    if (_session == null) {
      val cluster_builder =
        new Cluster.Builder().withClusterName("Tunihack Demo")
      var cluster: Cluster = null
      cluster = cluster_builder
        .addContactPoint(DB_HOST)
        .withPort(DB_PORT.toInt)
        .withCredentials(DB_USER, DB_PASS)
        .build()

      cluster.getConfiguration.getCodecRegistry
        .register(LocalDateCodec.instance)

      val queryLogger: QueryLogger = QueryLogger.builder().build()

      cluster.register(queryLogger)

      _session = cluster.connect("tunihack")
    }

    _session
  }
}

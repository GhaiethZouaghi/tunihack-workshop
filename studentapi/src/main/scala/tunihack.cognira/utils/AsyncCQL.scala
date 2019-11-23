package tunihack.cognira.utils

import com.datastax.driver.core._
import com.google.common.util.concurrent.{
  FutureCallback,
  Futures,
  ListenableFuture
}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.implicitConversions
import monix.eval.Task
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global
import scala.util.{Try, Success, Failure}
import scala.collection.mutable._

import tunihack.cognira.persistence.CassandraSessionProvider

object AsyncCQL {

  var cache: HashMap[String, Future[PreparedStatement]] =
    HashMap[String, Future[PreparedStatement]]()

  val cassandraSession = CassandraSessionProvider.cassandraConn

  /**
    * Value Class to allow creation of CQL statements in the following manner:
    * cql"SELECT * FROM table WHERE key = ?"
    * @param context
    */
  implicit class CqlStrings(val context: StringContext) extends AnyVal {

    /**
      * Create a Cassandra PreparedStatement to run asynchronously
      * @param args
      * @param session
      * @return Future[PreparedStatement]
      */
    def cql(args: Any*)(
      implicit session: Session = cassandraSession
    ): Future[PreparedStatement] = {
      val key = context.raw(args: _*)
      if (!cache.contains(key)) {
        val statement = new SimpleStatement(key)
        val prepared_statement: Future[PreparedStatement] =
          session.prepareAsync(statement)
        cache.put(key, prepared_statement)
        prepared_statement
      } else {
        cache(key)
      }
    }
  }

  /**
    * Convert Guava's ListenableFuture to Scala Future
    * This function is implicit meaning that any ListenableFuture will be automatically
    * converted into a Scala Future
    * @param listenableFuture
    * @tparam T
    * @return Future[T]
    */
  implicit def listenableFutureToFuture[T](
    listenableFuture: ListenableFuture[T]
  ): Future[T] = {
    val promise = Promise[T]()
    Futures.addCallback(listenableFuture, new FutureCallback[T] {
      def onFailure(error: Throwable): Unit = {
        promise.failure(error)
      }
      def onSuccess(result: T): Unit = {
        promise.success(result)
      }
    })
    promise.future
  }

  /**
    * Execute the Future PreparedStatement asynchronously
    * @param statement
    * @param params
    * @param executionContext
    * @param session
    * @return Future[ResultSet]
    */
  def execute(statement: Future[PreparedStatement], params: Any*)(
    implicit executionContext: ExecutionContext,
    session: Session = cassandraSession
  ): Future[ResultSet] = {
    Try {
      val parameters = params.map(_.asInstanceOf[Object])
      val bound: Future[BoundStatement] = statement.map(_.bind(parameters: _*))
      bound.flatMap(session.executeAsync(_))
    } match {
      case Success(f) => f
      case Failure(e) => Future.failed(e)
    }

  }

  /**
    * Fetch rows from ResultSet in streams.
    * @param cql
    * @param parameters
    * @param executionContext
    * @param cassandraSession
    * @return Observable[Row]
    */
  def query(cql: Future[PreparedStatement], parameters: Any*)(
    implicit executionContext: ExecutionContext,
    cassandraSession: Session = cassandraSession
  ): Observable[Row] = {
    val observable =
      Observable.fromAsyncStateAction[Future[ResultSet], ResultSet](
        nextResultSet =>
          Task.fromFuture(nextResultSet).flatMap { resultSet =>
            Task((resultSet, resultSet.fetchMoreResults))
        }
      )(execute(cql, parameters: _*))

    observable
      .takeWhile(rs => !rs.isExhausted)
      .flatMap { resultSet =>
        val rows = (1 to resultSet.getAvailableWithoutFetching) map (
          _ => resultSet.one
        )
        Observable.fromIterable(rows)
      }
  }

  /**
    * This function can be used when we are fetching one result only
    * @param cql
    * @param parameters
    * @param executionContext
    * @param cassandraSession
    * @return Future[Option[Row] ]
    */
  def queryOne(cql: Future[PreparedStatement], parameters: Any*)(
    implicit executionContext: ExecutionContext,
    cassandraSession: Session = cassandraSession
  ): Future[Option[Row]] = {
    val f: Future[ResultSet] = execute(cql, parameters: _*)
    f.map { rs =>
      Option(rs.one())
    }
  }

  /**
    * Converts an Observable[Row] to Future[List[T] ]
    * @param orows
    * @param converter
    * @tparam T
    * @return Future[List[T] ]
    */
  def futureList[T](orows: Observable[Row],
                    converter: Row => T): Future[List[T]] = {
    val obs: Observable[T] = orows.map(r => converter(r))
    val tlist: Task[List[T]] = obs.foldLeftL(List.empty[T])(_ :+ _)
    tlist.runAsync
  }

}

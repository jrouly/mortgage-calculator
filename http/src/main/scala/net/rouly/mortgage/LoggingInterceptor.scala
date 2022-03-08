package net.rouly.mortgage

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object LoggingInterceptor extends StrictLogging {
  def apply(route: Route)(implicit ec: ExecutionContext): Route = requestContext => {
    val startTimeMs = System.currentTimeMillis()
    val result = route(requestContext)

    def log(status: Option[Int] = None, exception: Option[Throwable] = None): Unit = {
      val stopTimeMs = System.currentTimeMillis()
      val durationMs = stopTimeMs - startTimeMs
      val logstring = Seq(
        Some(s"uri=${requestContext.request.uri.path}"),
        Some(s"durationMs=${durationMs}"),
        status.map(s => s"status=${s}"),
        exception.map(ex => s"ex=${ex.getMessage}")
      ).flatten.reduce(_ + " " + _)
      logger.info(logstring)
    }

    result.onComplete {
      case Success(Rejected(_)) => log(status = Some(404))
      case Success(Complete(response)) => log(status = Some(response.status.intValue()))
      case Failure(exception) => log(exception = Some(exception))
    }

    result
  }
}

package net.rouly.mortgage

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.StrictLogging
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object MortgageApp extends App with StrictLogging {

  implicit private val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "mortgage-app")
  implicit private val executionContext: ExecutionContext = system.executionContext

  private val config = ConfigSource.default.loadOrThrow[MortgageAppConfig]
  private val router = new MortgageAppRouter()
  private val routes = LoggingInterceptor(router.routes)
  private val server = Http().newServerAt(config.host, config.port).bind(routes)

  server.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      logger.info(s"Server online at http://${address.getHostString}:${address.getPort}/")
    case Failure(exception) =>
      logger.error(s"Failed to bind HTTP endpoint, terminating system.", exception)
      system.terminate()
  }
}

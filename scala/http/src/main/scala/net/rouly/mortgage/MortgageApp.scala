package net.rouly.mortgage

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.StrictLogging
import pureconfig.ConfigSource
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object MortgageApp extends App with StrictLogging {

  implicit private val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "mortgage-app")
  implicit private val executionContext: ExecutionContext = system.executionContext

  private val config = ConfigSource.default.loadOrThrow[MortgageConfig]
  private val router = new MortgageRouter()
  private val routes = LoggingInterceptor(router.routes)
  private val server = Http().newServerAt(config.host, config.port).bind(routes)

  logger.info(s"Server listening at http://${config.host}:${config.port}")
  logger.info(s"Press RETURN to stop...")

  StdIn.readLine()

  server
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}

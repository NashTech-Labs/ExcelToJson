package com.knoldus

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import pureconfig._
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

import scala.util._

object Application extends App with Service with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("template")

  import system.dispatcher

  val config: Configurations = ConfigSource.resources("application.conf")
    .withFallback(ConfigSource.systemProperties).load[Configurations] match {
    case Left(e: ConfigReaderFailures) =>
      throw new RuntimeException(s"Unable to load config, original error: ${e.prettyPrint()}")
    case Right(x) => x
  }

  val binding = Http().newServerAt(config.app.host, config.app.port).bind(routes())

  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(
        """
          |──────────────────────────────────────────────────────────────────────────────────────────────────────────
          |─██████████████─██████████████─████████████████───██████──██████─██████████─██████████████─██████████████─
          |─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░░░██───██░░██──██░░██─██░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─
          |─██░░██████████─██░░██████████─██░░████████░░██───██░░██──██░░██─████░░████─██░░██████████─██░░██████████─
          |─██░░██─────────██░░██─────────██░░██────██░░██───██░░██──██░░██───██░░██───██░░██─────────██░░██─────────
          |─██░░██████████─██░░██████████─██░░████████░░██───██░░██──██░░██───██░░██───██░░██─────────██░░██████████─
          |─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░░░██───██░░██──██░░██───██░░██───██░░██─────────██░░░░░░░░░░██─
          |─██████████░░██─██░░██████████─██░░██████░░████───██░░██──██░░██───██░░██───██░░██─────────██░░██████████─
          |─────────██░░██─██░░██─────────██░░██──██░░██─────██░░░░██░░░░██───██░░██───██░░██─────────██░░██─────────
          |─██████████░░██─██░░██████████─██░░██──██░░██████─████░░░░░░████─████░░████─██░░██████████─██░░██████████─
          |─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░██──██░░░░░░██───████░░████───██░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─
          |─██████████████─██████████████─██████──██████████─────██████─────██████████─██████████████─██████████████─
          |──────────────────────────────────────────────────────────────────────────────────────────────────────────
          |""".stripMargin)
      logger.info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      logger.error(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }

}

import sbt._

trait Versions {
  val typeConfVersion = "1.4.2"
  val akkaVersion = "2.6.18"
  val akkaHttpVersion = "10.2.7"
  val scalaLoggingVersion = "3.9.4"
  val pureConfigVersion = "0.17.1"
  val apachePoiVersion = "4.1.2"
  val logBackVersion="1.2.10"
}

object Dependencies extends Versions {

  val typeConf = "com.typesafe" % "config" % typeConfVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  val logging = "log4j" % "log4j" % "1.2.16"
  val logback = "ch.qos.logback" % "logback-classic" % logBackVersion

  val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
  val apachePoiXml = "org.apache.poi" % "poi-ooxml" % apachePoiVersion
  val apachePoi = "org.apache.poi" % "poi" % apachePoiVersion

  val commonDependencies = List(
    typeConf,
    scalaLogging,
    logging,
    logback,
    pureConfig,
    apachePoi,
    apachePoiXml
  )

  val serviceDependencies = List(actor, akkaStream, akkaHttp)

}

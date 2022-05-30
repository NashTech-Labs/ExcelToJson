package com.knoldus

case class Configurations(app: ApplicationConf)

case class ApplicationConf(host: String, port: Int)
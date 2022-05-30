package com.knoldus

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import com.knoldus.Util._
import com.typesafe.scalalogging.LazyLogging

import java.io.File
import scala.concurrent.Future

trait RequestHandler extends LazyLogging {

  def convertExcelToJson(file: Option[File]): Future[HttpResponse] = {
    if (file.isDefined) {
      logger.error(s"RequestHandler: Going to extract json from excel file.")
      val json = getJson(file.get)
      logger.info(s"RequestHandler: Going to save json: $json in project directory")
      writeJsonToFile("excelFile.json", json)
      Future.successful(HttpResponse.apply(entity = HttpEntity("excelFile.json saved in project directory")))
    } else {
      logger.error(s"RequestHandler: No File found.")
      Future.successful(HttpResponse.apply(status = StatusCodes.Conflict, entity = HttpEntity("No file found!")))
    }
  }

}
package com.knoldus

import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix, put}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import com.knoldus.Application.system
import com.knoldus.Util._

import java.io.File
import scala.collection.immutable.ListMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

trait Service extends RequestHandler {

  def routes(): Route = pathPrefix("template") {
    path("excel-to-json") {
      (put & entity(as[Multipart.FormData])) { formData =>
        val extractedData: Future[Map[String, Any]] =
          formData.parts
            .mapAsync[(String, Any)](5) {
              case excelFile: BodyPart
                if excelFile.name == "excelFile" =>
                val (extension, fileName) =
                  getExtensionAndFileName(excelFile)
                val tempFile: File =
                  File.createTempFile(
                    s"$fileName",
                    s"excelFile." + extension.apply(1))
                excelFile.entity.dataBytes
                  .runWith(FileIO.toPath(tempFile.toPath))
                  .map { _ =>
                    s"excelFile" -> tempFile
                  }
            }
            .runFold(ListMap.empty[String, Any])((map, tuple) =>
              map + tuple)
        complete(
          extractedData
            .flatMap { data =>
              convertExcelToJson(data.get("excelFile").map(_.asInstanceOf[File]))
            }
            .recover {
              case _: Exception =>
                HttpResponse.apply(status = StatusCodes.BadRequest)
            }
        )
      }
    }
  }

}
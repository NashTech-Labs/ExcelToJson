package com.knoldus

import akka.http.scaladsl.model.Multipart
import com.typesafe.scalalogging.LazyLogging
import org.apache.poi.ss.usermodel.{Cell, CellType, Row}
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}

import java.io.{File, FileInputStream, FileWriter, IOException}

object Util extends LazyLogging {

  def getExtensionAndFileName(formDataPart: Multipart.FormData.BodyPart): (Array[String], String) = {
    (formDataPart.entity.getContentType().toString.split("/"),
      formDataPart
        .getFilename()
        .orElse("excelFile")
        .split("\\.")
        .apply(0))
  }

  def getJson(file: File): String = {
    var jsonString =
      """{
        |""".stripMargin
    val fis: FileInputStream = new FileInputStream(file)
    val wb = new XSSFWorkbook(fis)
    val sheet: XSSFSheet = wb.getSheetAt(0)
    val itr = sheet.iterator
    while ( {
      itr.hasNext
    }) {
      val row: Row = itr.next
      val cellIterator = row.cellIterator
      var count = 0
      while ( {
        cellIterator.hasNext
      }) {
        val cell: Cell = cellIterator.next
        cell.getCellType match {
          case CellType.STRING =>
            if (count == 0) {
              jsonString =
                s"""$jsonString"${cell.getStringCellValue}":""".stripMargin
              count = 1
            } else {
              jsonString =
                s"""$jsonString"${cell.getStringCellValue}"""".stripMargin
              jsonString = if (itr.hasNext) {
                s"""$jsonString,
                   |""".stripMargin
              } else {
                jsonString
              }
              count = 0
            }
        }
      }
    }
    jsonString =
      s"""$jsonString
         |}""".stripMargin
    jsonString.replaceAll("\n", "")
  }

  def writeJsonToFile(fileName: String, fileData: String): Unit = {
    try {
      val myWriter = new FileWriter(fileName)
      myWriter.write(fileData)
      myWriter.close()
      logger.info(s"Successfully added json to the file: $fileName.")
    } catch {
      case e: IOException =>
        logger.error("An error occurred.")
        e.printStackTrace()
    }
  }

}

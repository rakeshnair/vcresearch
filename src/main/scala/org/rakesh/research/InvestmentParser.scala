package org.rakesh.research

import java.io.{BufferedWriter, File, FileWriter}

import scala.io.Source
import scala.util.matching.Regex

/**
  * Format of each investment entry:
  * Month, Year
  * CompanyName
  * DollarValueOfFunding / TypeOfFunding
  *
  * Example:
  * May, 2000
  * MyAwesomeStartup
  * $8M / Series A
  *
  * Things to do on adding a new file to src/main/resources:
  * - Replace "  -" at the end of line with empty
  * - If Partner information is there (search for " and"), then cut it and place it on the
  * same line as Round
  */
object InvestmentParser {
  def main(args: Array[String]) {
    val resourceDir = "src/main/resources/"
    val filesInResourceDir = new File(resourceDir).list()
    val companyInvestments = scala.collection.mutable.ArrayBuffer.empty[CompanyInvestment]
    filesInResourceDir.foreach(f => {
      val file = new File(s"${resourceDir}${f}").getAbsoluteFile
      val lines = Source.fromFile(file).getLines().toSeq
      val pattern = new Regex("[a-zA-Z ,]*(19|20)\\d{2}$")
      val buffer = scala.collection.mutable.ArrayBuffer.empty[String]
      var j = 0
      for (i <- lines.indices) {
        if (j % 3 == 0) {
          if (!pattern.pattern.matcher(lines(i).trim).matches()) {
            val last = buffer.last
            buffer.remove(buffer.size - 1)
            val newLast = s"${last}, ${lines(i)}"
            buffer += newLast
            j -= 1
          } else {
            buffer += lines(i)
          }
        } else {
          buffer += lines(i)
        }
        j += 1
      }

      val lineGroupings = buffer.toSeq.grouped(3).toSeq
      val Investors = file.getName.split(".txt")(0)
      for (i <- lineGroupings.indices) {
        try {
          val current = lineGroupings(i)
          val name = current(1).trim
          val monthYear = MonthYear(current(0).trim)
          val fundValue = FundValue(current(2).trim).get
          val currentCompanyInvestment = CompanyInvestment(name,
            monthYear.get.month,
            monthYear.get.year,
            fundValue.denomination,
            fundValue.value,
            fundValue.series,
            Investors)
          companyInvestments += currentCompanyInvestment
        } catch {
          case e: Exception => {
            println(s"Exception: ${e.getMessage}. LineGrouping: ${lineGroupings(i)}")
          }
        }
      }
    })

    val fileName = "/Users/rakesh/Desktop/InvestmentCatalog.txt"
    val file = new File(fileName)
    val writer = new BufferedWriter(new FileWriter(file))
    try {
      writer.write(companyInvestments.mkString("\n"))
    } finally {
      writer.close()
    }
  }
}
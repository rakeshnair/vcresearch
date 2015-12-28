package org.rakesh.research

import scala.util.matching.Regex

object MonthYear {
  def apply(date: String): Option[MonthYear] = {
    val yearRegex = new Regex("^\\d{4}$")
    val splits = date.split(",")
    if (splits.length == 2) {
      return Some(MonthYear(splits(0).trim, splits(1).trim.toInt))
    } else {
      if (yearRegex.pattern.matcher(date.trim).matches()) {
        return Some(MonthYear("None", date.trim().toInt))
      }
    }
    None
  }
}

case class MonthYear(month: String, year: Int)
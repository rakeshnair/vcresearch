package org.rakesh.research

object FundValue {
  def apply(funding: String): Option[FundValue] = {
    val fundValueRegex = """([0-9]+)""".r
    val fundDenomRegex = """([M|K]$)""".r
    val trimmedFundingStr = funding.trim
    val splits = trimmedFundingStr.split("/")
    val candidateFundValue = splits(0).trim().toUpperCase()
    val fundValue = fundValueRegex.findFirstIn(candidateFundValue).getOrElse("-1.0")
    val fundDenom = fundDenomRegex.findFirstIn(candidateFundValue).getOrElse("U")
    val series = splits(1).trim
    return Some(FundValue(fundValue.toDouble, fundDenom, series))
  }
}

case class FundValue(value: Double, denomination: String, series: String)
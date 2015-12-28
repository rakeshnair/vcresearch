package org.rakesh.research

import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._

case class CompanyInvestment(
                              name: String,
                              month: String,
                              year: Int,
                              denomination: String,
                              value: Double,
                              series: String,
                              investors: String
                            ) {
  implicit val formats = Serialization.formats(NoTypeHints)

  override def toString = write(this)
}
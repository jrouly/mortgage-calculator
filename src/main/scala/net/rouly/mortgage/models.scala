package net.rouly.mortgage

import java.time.Period

case class DownPayment(
  amount: BigDecimal,
  percent: BigDecimal
)

case class Mortgage(
  principle: BigDecimal,
  rate: BigDecimal,
  term: Period
)

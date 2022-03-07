package net.rouly.mortgage

object Currency {
  implicit class RichBigDecimal(bd: BigDecimal) {
    val toCurrency: BigDecimal = bd.setScale(2, BigDecimal.RoundingMode.HALF_UP)
  }
}

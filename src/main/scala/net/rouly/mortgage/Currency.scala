package net.rouly.mortgage

object Currency {
  implicit class RichBigDecimal(bd: BigDecimal) {
    val toCurrency: BigDecimal = bd.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    val toPercent: BigDecimal = (bd * 100).setScale(0, BigDecimal.RoundingMode.HALF_UP)
  }
}

package net.rouly.mortgage

object Currency {
  implicit class RichBigDecimal(bd: BigDecimal) {
    val toCurrency: String = "$" + bd.setScale(2, BigDecimal.RoundingMode.HALF_UP)
    val toPercent: String = (bd * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP) + "%"
  }
}

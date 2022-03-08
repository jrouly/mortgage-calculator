package net.rouly.mortgage

import net.rouly.mortgage.Currency._
import org.scalatest.flatspec._
import org.scalatest.matchers.should._

class CurrencySpec extends AnyFlatSpec with Matchers {

  "big decimals" should "round to valid currency" in {
    BigDecimal(123.456789).toCurrency shouldEqual 123.46
    BigDecimal(0.0).toCurrency shouldEqual 0
    BigDecimal(0.00001).toCurrency shouldEqual 0
  }

}

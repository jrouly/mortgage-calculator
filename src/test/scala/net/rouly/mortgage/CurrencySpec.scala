package net.rouly.mortgage

import net.rouly.mortgage.Currency.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.should.*

import java.time.Period

class CurrencySpec extends AnyFlatSpec with Matchers {

  "big decimals" should "round to valid currency" in {
    BigDecimal(123.456789).toCurrency shouldEqual 123.46
    BigDecimal(0.0).toCurrency shouldEqual 0
    BigDecimal(0.00001).toCurrency shouldEqual 0
  }

}

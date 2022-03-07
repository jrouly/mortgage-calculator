package net.rouly.mortgage

import org.scalatest.flatspec._
import org.scalatest.matchers.should._

class IncomeTaxSpec extends AnyFlatSpec with Matchers {

  val bracket = IncomeTax.Bracket(lower = 100, upper = 1000, rate = 0.5)

  "income below minimum bracket" should "not be counted" in {
    bracket(10) shouldEqual 0
  }

  "income above minimum bracket but below upper bracket" should "be counted" in {
    bracket(500) shouldEqual 200
  }

  "income above maximum bracket" should "be counted up to the maximum bracket" in {
    bracket(1500) shouldEqual 450
  }

  val brackets = Seq(
    IncomeTax.Bracket(lower = 100, upper = 1000, rate = 0.1),
    IncomeTax.Bracket(lower = 2000, upper = 3000, rate = 0.2),
    IncomeTax.Bracket(lower = 3000, upper = 10000, rate = 0.3),
  )

  "income below all brackets" should "not be counted" in {
    IncomeTax.computeTax(brackets)(50) shouldEqual 0
  }

  "income within the first bracket" should "be counted" in {
    IncomeTax.computeTax(brackets)(500) shouldEqual 40
  }

  "income within the third bracket" should "be progressively taxed" in {
    IncomeTax.computeTax(brackets)(9000) shouldEqual 2090
  }
}

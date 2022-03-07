package net.rouly.mortgage

import net.rouly.mortgage.Currency.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.should.*

import java.time.Period

class LoanSpec extends AnyFlatSpec with Matchers {

  "monthly payment" should "be correctly calculated" in {
    Loan
      .monthlyPayment(
        totalPrinciple = 520000,
        annualInterestRate = 0.04,
        term = Period.ofYears(30)
      )
      .toCurrency shouldEqual 2482.56
  }

  "total principal" should "be correctly calculated" in {
    Loan
      .principleFromMonthlyPayment(
        monthlyPayment = 2000,
        annualInterestRate = 0.04,
        term = Period.ofYears(30)
      )
      .toCurrency shouldEqual 418922.48
  }

  "monthly payment" should "map to total principal" in {
    val principle = 520000
    val interestRate = 0.04
    val term = Period.ofYears(30)

    val monthlyPayment = Loan
      .monthlyPayment(
        totalPrinciple = principle,
        annualInterestRate = interestRate,
        term = term
      )

    Loan.principleFromMonthlyPayment(
      monthlyPayment = monthlyPayment,
      annualInterestRate = interestRate,
      term = term
    ) shouldEqual principle
  }
}

package net.rouly.mortgage

import java.time.Period

object Loan {

  /** Compute monthly repayment amount.
    * @return total principle + interest payment, monthly
    * @see https://www.businessinsider.com/personal-finance/how-to-calculate-mortgage-payment
    */
  def monthlyPayment(
    totalPrinciple: BigDecimal,
    annualInterestRate: BigDecimal,
    term: Period
  ): BigDecimal = {
    val monthlyRate = annualInterestRate / 12
    val payments = term.toTotalMonths

    val P = totalPrinciple
    val i = monthlyRate
    val n = payments

    val x = (1 + i).pow(n.toInt)
    (P * (i * x)) / (x - 1)
  }

  /** Compute the initial principle from the monthly repayment amount.
    * @return total loan principle
    * @see https://www.businessinsider.com/personal-finance/how-to-calculate-mortgage-payment
    */
  def principleFromMonthlyPayment(
    monthlyPayment: BigDecimal,
    annualInterestRate: BigDecimal,
    term: Period
  ): BigDecimal = {
    val monthlyRate = annualInterestRate / 12
    val payments = term.toTotalMonths

    val M = monthlyPayment
    val i = monthlyRate
    val n = payments

    val x = (1 + i).pow(n.toInt)
    (M * (x - 1)) / (i * x)
  }

}

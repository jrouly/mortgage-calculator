package net.rouly.mortgage

import java.time.Period

object Mortgage extends App {

  def monthlyPayment(
    homeValue: BigDecimal,
    downPayment: Either[BigDecimal, BigDecimal],
    interestRate: BigDecimal,
    mortgageTerm: Period,
    annualPropertyTaxRate: BigDecimal
  ): BigDecimal = {
    val downPaymentAmount = downPayment.fold(identity, _ * homeValue)
    val loanPrinciple = homeValue - downPaymentAmount
    val loanPayment = Loan.monthlyPayment(loanPrinciple, interestRate, mortgageTerm)
    val propertyTax = annualPropertyTaxRate * homeValue
    val monthlyPropertyTax = propertyTax / 12

    loanPayment + monthlyPropertyTax
  }

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: BigDecimal, propertyTax: BigDecimal): BigDecimal = {
    val interestDeduction = interest.min(750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = propertyTax.min(10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }
}

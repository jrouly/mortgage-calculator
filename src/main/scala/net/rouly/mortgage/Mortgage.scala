package net.rouly.mortgage

object Mortgage extends App {

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: BigDecimal, propertyTax: BigDecimal): BigDecimal = {
    val interestDeduction = interest.min(750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = propertyTax.min(10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }
}

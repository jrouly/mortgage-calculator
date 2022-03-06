package net.rouly.mortgage

object Mortgage extends App {

  /** Compute the monthly loan repayment amount.
    * @param amount total loan principle
    * @param rate annual interest rate
    * @param term term of the loan in years
    * @return total principle + interest payment, monthly
    * @see https://www.businessinsider.com/personal-finance/how-to-calculate-mortgage-payment
    */
  def monthlyLoanPaymentAmount(
    amount: Double,
    rate: Double,
    term: Int
  ): Double = {
    val monthlyRate = rate / 12
    val payments = term * 12

    val P = amount
    val i = monthlyRate
    val n = payments

    val x = Math.pow(1 + i, n)
    (P * (i * x)) / (x - 1)
  }

  /** Compute the initial loan principle from the monthly repayment amount.
    * @param monthlyPayment monthly loan repayment (principle + interest)
    * @param rate annual interest rate
    * @param term term of the loan in years
    * @return total loan principle
    * @see https://www.businessinsider.com/personal-finance/how-to-calculate-mortgage-payment
    */
  def loanPrincipleFromMonthlyPaymentAmount(
    monthlyPayment: Double,
    rate: Double,
    term: Int
  ): Double = {
    val monthlyRate = rate / 12
    val payments = term * 12

    val M = monthlyPayment
    val i = monthlyRate
    val n = payments

    val x = Math.pow(1 + i, n)
    (M * (x - 1)) / (i * x)
  }

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: Double, propertyTax: Double): Double = {
    val interestDeduction = Math.min(interest, 750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = Math.min(propertyTax, 10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }
}

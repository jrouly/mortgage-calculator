package net.rouly
package mortgage

object Constants {
  final val FederalIncomeTaxBrackets = Seq( // 2021.
    IncomeTax.Bracket(lower = 0, upper = 37650, rate = 0),
    IncomeTax.Bracket(lower = 37650, upper = 91150, rate = 0.25),
    IncomeTax.Bracket(lower = 91150, upper = 190150, rate = 0.28),
    IncomeTax.Bracket(lower = 190150, upper = 413350, rate = 0.33)
  )

  final val PropertyTax = 0.01 // National average.
}

object IncomeTax {

  def federal(income: Double): Statement = apply(Constants.FederalIncomeTaxBrackets)(income)

  def apply(brackets: Seq[Bracket])(income: Double): Statement = {
    val tax = computeTax(brackets)(income)
    Statement(
      income = income,
      tax = tax,
      effectiveRate = tax / income
    )
  }

  def computeTax(brackets: Seq[Bracket])(income: Double): Double = brackets.foldLeft(0d) {
    case (agg, bracket) => agg + bracket(income)
  }

  case class Statement(
    income: Double,
    tax: Double,
    effectiveRate: Double
  )

  case class Bracket(lower: Double, upper: Double, rate: Double) {
    def apply(income: Double): Double = {
      val applicable = Math.max(Math.min(upper, income), lower) - lower
      applicable * rate
    }
  }
}

object Mortgage extends App {

  println(
    loanPrincipleFromMonthlyPaymentAmount(3400, 0.04, 30)
  )

  def mortgage_old(
    annualGrossIncome: Double,
    purchasePrice: Double,
    downPayment: Either[Double, Double],
    loanRate: Double,
    hoa: Double
  ): Double = {
    // Intermediate.
    val monthlyGrossIncome: Double = monthly(annualGrossIncome)
    val annualFederalIncomeTax: IncomeTax.Statement = IncomeTax.federal(annualGrossIncome)
    val monthlyFederalIncomeTax: Double = monthly(annualFederalIncomeTax.tax)

    val principle = totalLoanAmount(purchasePrice, downPayment)
    val interest = loanRate * principle
    val propertyTax = Constants.PropertyTax * purchasePrice

    val totalMonthlyInvestment = principle + interest + propertyTax + hoa
    val totalMonthlyCost = totalMonthlyInvestment - taxDeduction(interest, propertyTax)

    totalMonthlyCost
  }

  // Functions.
  def monthly(x: Double): Double = x / 12

//  private def purchaseBudget(
//    annualGrossIncome: Double,
//    downPayment: Double,
//    rate: Double,
//    term: Int,
//    targetHousingDebtToIncomeRatio: Double = 0.28,
//  ): Double = {
//    val monthlyGrossIncome = annualGrossIncome / 12
//    val targetMonthlyPayment = targetHousingDebtToIncomeRatio * monthlyGrossIncome
//  }

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: Double, propertyTax: Double): Double = {
    val interestDeduction = Math.min(interest, 750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = Math.min(propertyTax, 10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }

  //  def estimate(
//    annualGrossIncome: Double,
//    downPayment: Double,
//    loanRate: Double = 0.04,
//    targetPaymentRatio: Double = 0.28,
//  ): Double = {
//    val targetMonthlyPayment = targetPaymentRatio * monthly(annualGrossIncome)
//
//  }

  def totalLoanAmount(purchasePrice: Double, downPayment: Either[Double, Double]): Double = {
    downPayment match {
      case Left(percent) => purchasePrice - (purchasePrice * percent)
      case Right(dollarValue) => purchasePrice - dollarValue
    }
  }

  /** Compute the monthly loan repayment amount.
    * @param amount total loan principle
    * @param rate annual interest rate
    * @param term term of the loan in years
    * @return total principle + interest payment, monthly
    * @see https://www.businessinsider.com/personal-finance/how-to-calculate-mortgage-payment
    */
  private def monthlyLoanPaymentAmount(
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
  private def loanPrincipleFromMonthlyPaymentAmount(
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

  private def monthlyIncomeTax(
    annualGrossIncome: Double
  ): Double = {
    monthly(IncomeTax.federal(annualGrossIncome).tax)
  }
}

package net.rouly
package mortgage

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

  object Constants {
    final val FederalIncomeTaxBrackets = Seq( // 2021.
      IncomeTax.Bracket(lower = 0, upper = 37650, rate = 0),
      IncomeTax.Bracket(lower = 37650, upper = 91150, rate = 0.25),
      IncomeTax.Bracket(lower = 91150, upper = 190150, rate = 0.28),
      IncomeTax.Bracket(lower = 190150, upper = 413350, rate = 0.33)
    )

    final val PropertyTax = 0.01 // National average.
  }
}

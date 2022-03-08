package net.rouly.mortgage

object IncomeTax {

  /** Compute federal income tax statement from annual gross income. */
  def federal(income: BigDecimal): Statement = Statement(Constants.FederalIncomeTaxBrackets)(income)

  /** Compute the amount of income tax using a defined set of brackets for an annual gross income.
    * @param brackets tax brackets
    * @param income annual gross income
    * @return tax bill
    */
  def computeTax(brackets: Seq[Bracket])(income: BigDecimal): BigDecimal =
    brackets.foldLeft(BigDecimal(0)) { case (agg, bracket) =>
      agg + bracket(income)
    }

  case class Statement(
    income: BigDecimal,
    tax: BigDecimal,
    effectiveRate: BigDecimal
  )

  case class Bracket(lower: BigDecimal, upper: BigDecimal, rate: BigDecimal) {
    def apply(income: BigDecimal): BigDecimal = {
      val applicable = upper.min(income).max(lower) - lower
      applicable * rate
    }
  }

  object Statement {
    def apply(brackets: Seq[Bracket])(income: BigDecimal): Statement = {
      val tax = computeTax(brackets)(income)
      Statement(
        income = income,
        tax = tax,
        effectiveRate = tax / income
      )
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

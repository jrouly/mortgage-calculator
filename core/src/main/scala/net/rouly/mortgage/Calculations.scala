package net.rouly.mortgage

import java.time.Period

object Calculations {

  object AffordableBudget {

    /** Determine maximum affordable home price.
      * @param annualGrossIncome annual gross income
      * @param debtIncomeRatio target monthly payment (debt) to monthly income ratio
      * @param downPayment liquid capital prepared for a down payment
      * @param mortgageRate mortgage loan annual interest rate
      * @param mortgageTerm mortgage loan term
      * @param propertyTaxRate annual property tax rate
      * @return estimated maximum affordable home price
      */
    def apply(
      annualGrossIncome: BigDecimal,
      debtIncomeRatio: BigDecimal,
      downPayment: BigDecimal,
      mortgageRate: BigDecimal,
      mortgageTerm: Period,
      propertyTaxRate: BigDecimal
    ): Breakdown = {
      val monthlyGrossIncome = annualGrossIncome / 12
      val monthlyPayment = monthlyGrossIncome * debtIncomeRatio
      val monthlyPropertyTax = estimateMonthlyPropertyTax(
        monthlyPayment = monthlyPayment,
        downPayment = downPayment,
        mortgageRate = mortgageRate,
        mortgageTerm = mortgageTerm,
        propertyTaxRate = propertyTaxRate
      )
      val monthlyLoanRepayment = monthlyPayment - monthlyPropertyTax
      val loanPrinciple =
        Loan.principleFromMonthlyPayment(monthlyLoanRepayment, mortgageRate, mortgageTerm)
      val homeValue = loanPrinciple + downPayment
      val downPaymentPercent = downPayment / homeValue

      Breakdown(
        annualGrossIncome = annualGrossIncome,
        monthlyGrossIncome = monthlyGrossIncome,
        debtIncomeRatio = debtIncomeRatio,
        downPaymentAmount = downPayment,
        downPaymentPercent = downPaymentPercent,
        monthlyPayment = monthlyPayment,
        monthlyLoanRepayment = monthlyLoanRepayment,
        monthlyPropertyTax = monthlyPropertyTax,
        loanPrinciple = loanPrinciple,
        homeValue = homeValue,
        annualMortgageRate = mortgageRate,
        mortgageTerm = mortgageTerm,
        annualPropertyTaxRate = propertyTaxRate,
        annualPropertyTax = monthlyPropertyTax * 12,
      )
    }

    private def estimateMonthlyPropertyTax(
      monthlyPayment: BigDecimal,
      downPayment: BigDecimal,
      mortgageRate: BigDecimal,
      mortgageTerm: Period,
      propertyTaxRate: BigDecimal
    ): BigDecimal = {
      val estimates = for (propertyTax <- 0 to monthlyPayment.intValue) yield {
        val loanRepayment = monthlyPayment - propertyTax
        val principle = Loan.principleFromMonthlyPayment(loanRepayment, mortgageRate, mortgageTerm)
        val homeValue = principle + downPayment

        val annualPropertyTax = homeValue * propertyTaxRate
        val monthlyPropertyTax = annualPropertyTax / 12

        propertyTax -> (monthlyPropertyTax - propertyTax).abs
      }

      estimates.minBy { case (propertyTax, delta) => delta }._1
    }

    case class Breakdown(
      annualGrossIncome: BigDecimal,
      monthlyGrossIncome: BigDecimal,
      debtIncomeRatio: BigDecimal,
      downPaymentAmount: BigDecimal,
      downPaymentPercent: BigDecimal,
      monthlyPayment: BigDecimal,
      monthlyLoanRepayment: BigDecimal,
      monthlyPropertyTax: BigDecimal,
      loanPrinciple: BigDecimal,
      homeValue: BigDecimal,
      annualMortgageRate: BigDecimal,
      mortgageTerm: Period,
      annualPropertyTaxRate: BigDecimal,
      annualPropertyTax: BigDecimal,
    )

  }

  object MonthlyPayment {

    /** Determine the monthly payment of a mortgage.
      * @param homeValue total home value
      * @param downPayment either a cash value or a percentage
      * @param mortgageRate mortgage loan annual interest rate
      * @param mortgageTerm mortgage loan term
      * @param propertyTaxRate annual property tax rate
      * @return estimated monthly payment
      */
    def apply(
      homeValue: BigDecimal,
      downPayment: Either[BigDecimal, BigDecimal],
      mortgageRate: BigDecimal,
      mortgageTerm: Period,
      propertyTaxRate: BigDecimal
    ): Breakdown = {
      val downPaymentAmount = downPayment.fold(identity, _ * homeValue)
      val downPaymentPercent = downPaymentAmount / homeValue

      val loanPrinciple = homeValue - downPaymentAmount
      val monthlyLoanRepayment = Loan.monthlyPayment(loanPrinciple, mortgageRate, mortgageTerm)
      val annualPropertyTax = propertyTaxRate * homeValue
      val monthlyPropertyTax = annualPropertyTax / 12
      val monthlyPayment = monthlyLoanRepayment + monthlyPropertyTax

      Breakdown(
        annualPropertyTax = annualPropertyTax,
        downPaymentAmount = downPaymentAmount,
        downPaymentPercent = downPaymentPercent,
        homeValue = homeValue,
        loanPrinciple = loanPrinciple,
        monthlyLoanRepayment = monthlyLoanRepayment,
        monthlyPayment = monthlyPayment,
        monthlyPropertyTax = monthlyPropertyTax,
        annualMortgageRate = mortgageRate,
        mortgageTerm = mortgageTerm,
        annualPropertyTaxRate = propertyTaxRate
      )
    }

    case class Breakdown(
      annualPropertyTax: BigDecimal,
      downPaymentAmount: BigDecimal,
      downPaymentPercent: BigDecimal,
      homeValue: BigDecimal,
      loanPrinciple: BigDecimal,
      monthlyLoanRepayment: BigDecimal,
      monthlyPayment: BigDecimal,
      monthlyPropertyTax: BigDecimal,
      annualMortgageRate: BigDecimal,
      mortgageTerm: Period,
      annualPropertyTaxRate: BigDecimal
    )
  }
}

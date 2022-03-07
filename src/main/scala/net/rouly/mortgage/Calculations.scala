package net.rouly.mortgage

import net.rouly.mortgage.Currency.*

import java.math.MathContext
import java.time.Period
import scala.math.BigDecimal.RoundingMode

object Calculations {

  /** Determine the monthly payment of a mortgage.
    * @param homeValue total home value
    * @param downPayment either a cash value or a percentage
    * @param mortgageRate mortgage loan annual interest rate
    * @param mortgageTerm mortgage loan term
    * @param propertyTaxRate annual property tax rate
    * @return estimated monthly payment
    */
  def calculateMonthlyPayment(
    homeValue: BigDecimal,
    downPayment: Either[BigDecimal, BigDecimal],
    mortgageRate: BigDecimal,
    mortgageTerm: Period,
    propertyTaxRate: BigDecimal,
  ): BigDecimal = {
    val downPaymentAmount = downPayment.fold(identity, _ * homeValue)
    val downPaymentPercent = downPaymentAmount / homeValue

    val loanPrinciple = homeValue - downPaymentAmount
    val monthlyLoanRepayment = Loan.monthlyPayment(loanPrinciple, mortgageRate, mortgageTerm)
    val annualPropertyTax = propertyTaxRate * homeValue
    val monthlyPropertyTax = annualPropertyTax / 12
    val monthlyPayment = monthlyLoanRepayment + monthlyPropertyTax

    println(s"INPUT")
    println(s"  home value: $$${homeValue.toCurrency}")
    println(s"  down payment: $$${downPaymentAmount.toCurrency} (${downPaymentPercent.toPercent}%)")
    println(s"  loan principle: $$${loanPrinciple.toCurrency}")
    println(s"OUTPUT")
    println(s"  total monthly payment: $$${monthlyPayment.toCurrency}")
    println(s"    - loan repayment: $$${monthlyLoanRepayment.toCurrency}")
    println(s"    - property tax: $$${monthlyPropertyTax.toCurrency}")

    monthlyPayment
  }

  /** Determine maximum affordable home price.
    * @param annualGrossIncome annual gross income
    * @param debtIncomeRatio target monthly payment (debt) to monthly income ratio
    * @param downPayment liquid capital prepared for a down payment
    * @param mortgageRate mortgage loan annual interest rate
    * @param mortgageTerm mortgage loan term
    * @param propertyTaxRate annual property tax rate
    * @return estimated maximum affordable home price
    */
  def calculateAffordableBudget(
    annualGrossIncome: BigDecimal,
    debtIncomeRatio: BigDecimal,
    downPayment: BigDecimal,
    mortgageRate: BigDecimal,
    mortgageTerm: Period,
    propertyTaxRate: BigDecimal
  ): BigDecimal = {
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
    val loanPrinciple = Loan.principleFromMonthlyPayment(monthlyLoanRepayment, mortgageRate, mortgageTerm)
    val homeValue = loanPrinciple + downPayment
    val downPaymentPercent = downPayment / homeValue

    println(s"INPUT")
    println(s"  annual gross income: $$${annualGrossIncome.toCurrency}")
    println(s"  monthly gross income: $$${monthlyGrossIncome.toCurrency}")
    println(s"  debt/income ratio: ${debtIncomeRatio.toPercent}%")
    println(s"  down payment: $$${downPayment.toCurrency} (${downPaymentPercent.toPercent}%)")
    println(s"OUTPUT")
    println(s"  total monthly payment: $$${monthlyPayment.toCurrency}")
    println(s"    - loan repayment: $$${monthlyLoanRepayment.toCurrency}")
    println(s"    - property tax: $$${monthlyPropertyTax.toCurrency} (estimate)")
    println(s"  loan principle: $$${loanPrinciple.toCurrency}")
    println(s"  home value: $$${homeValue.toCurrency}")

    homeValue
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

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: BigDecimal, propertyTax: BigDecimal): BigDecimal = {
    val interestDeduction = interest.min(750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = propertyTax.min(10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }
}

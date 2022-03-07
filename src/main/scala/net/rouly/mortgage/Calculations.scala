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
    val loanPayment = Loan.monthlyPayment(loanPrinciple, mortgageRate, mortgageTerm)
    val propertyTax = propertyTaxRate * homeValue
    val monthlyPropertyTax = propertyTax / 12

    loanPayment + monthlyPropertyTax + hoa
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
    val principle =
      Loan.principleFromMonthlyPayment(monthlyPayment, mortgageRate, mortgageTerm)

    principle + downPayment
  }

  /** Compute total tax deduction based on legal limits and input amounts. */
  private def taxDeduction(interest: BigDecimal, propertyTax: BigDecimal): BigDecimal = {
    val interestDeduction = interest.min(750 * 1000) // 2021 limit of 750k
    val propertyTaxDeduction = propertyTax.min(10 * 1000) // 2021 limit of 10k
    interestDeduction + propertyTaxDeduction
  }

  private def percent(bd: BigDecimal): BigDecimal = (bd * 100).setScale(0, RoundingMode.HALF_UP)
}

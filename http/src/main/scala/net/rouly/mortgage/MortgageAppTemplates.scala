package net.rouly.mortgage

import net.rouly.mortgage.Currency.RichBigDecimal

import scala.xml.Elem

object MortgageAppTemplates {

  def root(): String = base("mortgage calculator") {
    <div>
      <ul>
        <li><a href={MortgageAppRouter.paths.MonthlyPayment}>monthly payment</a></li>
        <li><a href={MortgageAppRouter.paths.Budget}>maximum affordable budget</a></li>
      </ul>
    </div>
  }.toString

  def monthlyPayment(breakdown: Option[Calculations.MonthlyPayment.Breakdown]): String = {
    def renderBreakdown(breakdown: Calculations.MonthlyPayment.Breakdown): Elem =
      <div>
        <table>
          <tr><td>Home value</td><td>{breakdown.homeValue.toCurrency}</td></tr>
          <tr><td>Down payment amount</td><td>{breakdown.downPaymentAmount.toCurrency}</td></tr>
          <tr><td>Down payment percent</td><td>{breakdown.downPaymentPercent.toPercent}</td></tr>
          <tr><td>Mortgage principle</td><td>{breakdown.loanPrinciple.toCurrency}</td></tr>
          <tr><td>Mortgage interest rate</td><td>{breakdown.annualMortgageRate.toPercent}</td></tr>
          <tr><td>Mortgage term in years</td><td>{breakdown.mortgageTerm.getYears.toString}</td></tr>
          <tr><td>Annual property tax rate</td><td>{breakdown.annualPropertyTaxRate.toPercent}</td></tr>
          <tr><td>Annual property tax payment</td><td>{breakdown.annualPropertyTax.toCurrency}</td></tr>
          <tr><td>Monthly payment (total)</td><td>{breakdown.monthlyPayment.toCurrency}</td></tr>
          <tr><td>Monthly payment: loan repayment</td><td>{breakdown.monthlyLoanRepayment.toCurrency}</td></tr>
          <tr><td>Monthly payment: property tax</td><td>{breakdown.monthlyPropertyTax.toCurrency}</td></tr>
        </table>
      </div>

    def renderForm: Elem =
      <form action={MortgageAppRouter.paths.MonthlyPayment} method="post">

        Total home value:
        <input
          type="text"
          name="home-value"
          value={breakdown.map(_.homeValue.toString).orNull}
        /><br />

        Down payment (percent):
        <input
          type="text"
          name="down-payment"
          value={breakdown.map(_.downPaymentPercent.toString).orNull}
          placeholder="0.20"
        /><br />

        Mortgage interest rate:
        <input
          type="text"
          name="mortgage-rate"
          value={breakdown.map(_.annualMortgageRate.toString).orNull}
          placeholder="0.04"
        /><br />

        Mortgage term in years:
        <input
          type="text"
          name="mortgage-years"
          value={breakdown.map(_.mortgageTerm.getYears.toString).orNull}
          placeholder="30"
        /><br />

        Property tax rate:
        <input
          type="text"
          name="property-tax"
          value={breakdown.map(_.annualPropertyTaxRate.toString).orNull}
          placeholder="0.01"
        /><br />

        <input type="submit" value="Submit" />
      </form>

    base("monthly payments") {
      <div>
        {renderForm}
        {breakdown.map(renderBreakdown).orNull}
      </div>
    }.toString
  }

  def budget(breakdown: Option[Calculations.AffordableBudget.Breakdown]): String = {
    def renderBreakdown(breakdown: Calculations.AffordableBudget.Breakdown): Elem =
      <div>
        <table>
          <tr><td>Annual gross income</td><td>{breakdown.annualGrossIncome.toCurrency}</td></tr>
          <tr><td>Monthly gross income</td><td>{breakdown.monthlyGrossIncome.toCurrency}</td></tr>
          <tr><td>Housing debt to income ratio</td><td>{breakdown.debtIncomeRatio.toPercent}</td></tr>
          <tr><td>Down payment amount</td><td>{breakdown.downPaymentAmount.toCurrency}</td></tr>
          <tr><td>Down payment percent</td><td>{breakdown.downPaymentPercent.toPercent}</td></tr>
          <tr><td>Monthly payment (total)</td><td>{breakdown.monthlyPayment.toCurrency}</td></tr>
          <tr><td>Monthly payment: loan repayment</td><td>{breakdown.monthlyLoanRepayment.toCurrency}</td></tr>
          <tr><td>Monthly payment: property tax</td><td>{breakdown.monthlyPropertyTax.toCurrency}</td></tr>
          <tr><td>Mortgage principle</td><td>{breakdown.loanPrinciple.toCurrency}</td></tr>
          <tr><td>Mortgage interest rate</td><td>{breakdown.annualMortgageRate.toPercent}</td></tr>
          <tr><td>Mortgage term in years</td><td>{breakdown.mortgageTerm.getYears.toString}</td></tr>
          <tr><td>Annual property tax rate</td><td>{breakdown.annualPropertyTaxRate.toPercent}</td></tr>
          <tr><td>Annual property tax payment</td><td>{breakdown.annualPropertyTax.toCurrency}</td></tr>
          <tr><td>Max home value budget</td><td>{breakdown.homeValue.toCurrency}</td></tr>
        </table>
      </div>

    def renderForm: Elem =
      <form action={MortgageAppRouter.paths.Budget} method="post">

        Annual gross income:
        <input
          type="text"
          name="annual-gross-income"
          value={breakdown.map(_.annualGrossIncome.toString).orNull}
        /><br />

        Housing debt to income ratio:
        <input
          type="text"
          name="debt-income-ratio"
          value={breakdown.map(_.debtIncomeRatio.toString).orNull}
          placeholder="0.28"
        /><br />

        Down payment amount:
        <input
          type="text"
          name="down-payment"
          value={breakdown.map(_.downPaymentAmount.toString).orNull}
        /><br />

        Mortgage rate:
        <input
          type="text"
          name="mortgage-rate"
          value={breakdown.map(_.annualMortgageRate.toString).orNull}
          placeholder="30"
        /><br />

        Mortgage term (years):
        <input
          type="text"
          name="mortgage-years"
          value={breakdown.map(_.mortgageTerm.getYears.toString).orNull}
          placeholder="0.01"
        /><br />

        Property tax rate:
        <input
          type="text"
          name="property-tax"
          value={breakdown.map(_.annualPropertyTaxRate.toString).orNull}
          placeholder="0.01"
        /><br />

        <input type="submit" value="Submit" />
      </form>

    base("affordable budget") {
      <div>
        {renderForm}
        {breakdown.map(renderBreakdown).orNull}
      </div>
    }.toString
  }

  private def base(title: String)(body: Elem): Elem =
    <html>
      <head><title>{title}</title></head>
      <body>
        <a href="/">home</a>
        {body}
      </body>
    </html>

}

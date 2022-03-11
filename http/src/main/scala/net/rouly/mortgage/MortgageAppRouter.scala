package net.rouly.mortgage

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import java.time.Period

class MortgageAppRouter extends Unmarshallers {

  def routes: Route = concat(
    root,
    monthlyPayment,
    budget
  )

  def root: Route = path("") {
    get {
      val body = MortgageAppTemplates.root()
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
    }
  }

  def monthlyPayment: Route = path(MortgageAppRouter.paths.MonthlyPayment) {
    concat(
      get {
        val body = MortgageAppTemplates.monthlyPayment(None)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
      },
      post {
        formFields(
          "home-value".as[BigDecimal],
          "down-payment".as[BigDecimal].withDefault(0.20),
          "mortgage-rate".as[BigDecimal].withDefault(0.04),
          "mortgage-years".as[Int].withDefault(30),
          "property-tax".as[BigDecimal].withDefault(0.01)
        ) { (homeValue, downPayment, mortgageRate, mortgageYears, propertyTaxRate) =>
          val breakdown = Calculations.MonthlyPayment(
            homeValue = homeValue,
            downPayment = if (downPayment > 1) Left(downPayment) else Right(downPayment),
            mortgageRate = mortgageRate,
            mortgageTerm = Period.ofYears(mortgageYears),
            propertyTaxRate = propertyTaxRate
          )
          val body = MortgageAppTemplates.monthlyPayment(Some(breakdown))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
        }
      }
    )
  }

  def budget: Route = path(MortgageAppRouter.paths.Budget) {
    concat(
      get {
        val body = MortgageAppTemplates.budget(None)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
      },
      post {
        formFields(
          "annual-gross-income".as[BigDecimal],
          "debt-income-ratio".as[BigDecimal].withDefault(0.28),
          "down-payment".as[BigDecimal],
          "mortgage-rate".as[BigDecimal].withDefault(0.04),
          "mortgage-years".as[Int].withDefault(30),
          "property-tax".as[BigDecimal].withDefault(0.01)
        ) { (agi, debtIncome, downPayment, mortgageRate, mortgageYears, propertyTax) =>
          val breakdown = Calculations.AffordableBudget(
            annualGrossIncome = agi,
            debtIncomeRatio = debtIncome,
            downPayment = downPayment,
            mortgageRate = mortgageRate,
            mortgageTerm = Period.ofYears(mortgageYears),
            propertyTaxRate = propertyTax
          )
          val body = MortgageAppTemplates.budget(Some(breakdown))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
        }
      }
    )
  }
}

object MortgageAppRouter {
  object paths {
    val MonthlyPayment = "monthly-payment"
    val Budget = "budget"
  }
}

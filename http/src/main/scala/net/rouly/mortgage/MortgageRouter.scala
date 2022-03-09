package net.rouly.mortgage

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import java.time.Period

class MortgageRouter {

  def routes: Route = concat(
    root,
    monthlyPayment,
    budget
  )

  def root: Route = path("") {
    get {
      val body =
        <html>
        <head><title>mortgage calculator</title></head>
        <body>
          <ul>
            <li><a href="/monthly-payment">monthly payment</a></li>
            <li><a href="/budget">maximum affordable budget</a></li>
          </ul>
        </body>
        </html>

      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body.toString))
    }
  }

  def monthlyPayment: Route = path("monthly-payment") {
    concat(
      get {
        parameters(
          "homeValue".as[Double],
          "downPayment".as[Double].withDefault(0.20),
          "mortgageRate".as[Double].withDefault(0.04),
          "mortgageYears".as[Int].withDefault(30),
          "propertyTaxRate".as[Double].withDefault(0.01)
        ) { (homeValue, downPayment, mortgageRate, mortgageYears, propertyTaxRate) =>
          val breakdown = Calculations.MonthlyPayment(
            homeValue = homeValue,
            downPayment = if (downPayment > 1) Left(downPayment) else Right(downPayment),
            mortgageRate = mortgageRate,
            mortgageTerm = Period.ofYears(mortgageYears),
            propertyTaxRate = propertyTaxRate,
          )

          val body =
            <html>
              <head><title>mortgage calculator</title></head>
              <body>
                total monthly payment: {breakdown.monthlyPayment}
              </body>
            </html>

          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body.toString))
        }
      },
    )
  }

  def budget: Route = path("budget") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "max budget"))
    }
  }

}

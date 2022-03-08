package net.rouly.mortgage

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class MortgageRouter {

  def routes: Route = concat(
    root,
    monthlyPayment,
    budget
  )

  def root: Route = path("") {
    get {
      val body =
        """
          |<html>
          |<head><title>mortgage-calculator</title></head>
          |<body>
          |Welcome to mortgage-calculator.
          |<ul>
          |<li><a href="/monthly-payment">calculate monthly payment</a></li>
          |<li><a href="/budget">calculate affordable budget</a></li>
          |</body>
          |</html>
          |""".stripMargin
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, body))
    }
  }

  def monthlyPayment: Route = path("monthly-payment") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "monthly payment"))
    }
  }

  def budget: Route = path("budget") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "max budget"))
    }
  }

}

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
      val body = MortgageService.root.render
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

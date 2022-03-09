package net.rouly.mortgage

import scalatags.Text
import scalatags.Text.all._

object MortgageService {

  def root: Text.TypedTag[String] = html(
    head(tag("title")("mortgage-calculator")),
    body(
      ul(
        li(a(href := "/monthly-payment")("calculate monthly payment")),
        li(a(href := "/budget")("calculate affordable budget"))
      )
    )
  )

}

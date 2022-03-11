package net.rouly.mortgage

import akka.http.scaladsl.unmarshalling.Unmarshaller

trait Unmarshallers {

  implicit val bigDecimalFromStringUnmarshaller: Unmarshaller[String, BigDecimal] =
    Unmarshaller.strict(BigDecimal.apply)

}

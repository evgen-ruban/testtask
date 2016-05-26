package org.accounts.model

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol


object CustomJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val AccountFormat = jsonFormat12(Account)
  implicit val ResponseStringFormat = jsonFormat2(ResponseString)
  implicit val ResponseAccountSeqFormat = jsonFormat2(ResponseAccountSeq)
  implicit val ResponseStringSeqFormat = jsonFormat2(ResponseStringSeq)
  implicit val ResponseIntFormat = jsonFormat2(ResponseInt)
}

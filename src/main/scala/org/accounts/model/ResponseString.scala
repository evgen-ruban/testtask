package org.accounts.model


trait Response[T] {
  val code: Int
  val message: T
}
case class ResponseString(code: Int, message: String) extends Response[String]
case class ResponseStringSeq(code: Int, message: Seq[String]) extends Response[Seq[String]]
case class ResponseAccountSeq(code: Int, message: Seq[Account]) extends Response[Seq[Account]]
case class ResponseInt(code: Int, message: Int)  extends Response[Int]
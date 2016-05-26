package org.tests.accounts.spec

import org.accounts.model.CustomJsonProtocol._
import org.accounts.model.{Account, ResponseString}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, FlatSpec}
import reactivemongo.bson.BSONObjectID
import spray.http.ContentTypes._
import spray.http.HttpEntity
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._

class UpdatePasswordSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures  with Matchers with MockitoSugar with CommonResults {
  val url = "/update_password"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val accountFactory = Account(
    _: Option[String],
    enabled = Some(true),
    login = Some("ivanov111"),
    email = Some("ivanov@gmail.com"),
    name = Some("Ivan"),
    secondName = Some("Ivanov"),
    roles = Seq(),
    groups = Seq("5720a6bf56d06b2bbf907230"),
    permissions = Seq(),
    info = Some("some_info"),
    hash = Some("123456789"),
    sessionTime = Some(15)
  )

  "Password" should "be updated" in {

    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(someResult(account))
    when(accountRepository.updateAccountById(account.copy(hash = Some("new_hash")))).thenReturn(okUpdateWriteResult)

    Post(url, HttpEntity(`application/json`, s"""{"id": "$id", "hash": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Password" should "not be updated when ID not found" in {

    val id = BSONObjectID.generate.stringify

    when(accountRepository.findAccountById(Some(id))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"id": "$id", "hash": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This id doesn't match any document"))
    }
  }

}
package org.tests.accounts.spec

import org.accounts.model.CustomJsonProtocol._
import org.accounts.model.{ResponseString, Account, ResponseInt}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import spray.http.ContentTypes._
import spray.http.{HttpEntity, StatusCodes}
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest

class SessionTimeSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures  with Matchers with MockitoSugar with CommonResults {
  val url = "/session_time"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val accountFactory = Account(
    id = Some("my_id"),
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
    _: Option[Int]
  )

  "SessionTime" should "be 15" in {

    val account = accountFactory(Some(15))

    when(accountRepository.findAccountByLogin(account.login)).thenReturn(someResult(account))

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}"}""")) ~> routs ~> check {
      responseAs[ResponseInt] should be(ResponseInt(200, 15))
    }
  }

  "Response" should "be with 'Error' message because of empty sessionTime" in {

    val account = accountFactory(None)

    when(accountRepository.findAccountByLogin(account.login)).thenReturn(someResult(account))

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(400, "Error"))
    }
  }

}
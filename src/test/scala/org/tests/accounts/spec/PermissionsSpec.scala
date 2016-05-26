package org.tests.accounts.spec

import org.accounts.model.{ResponseStringSeq, ResponseString, ResponseInt, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.concurrent.ScalaFutures
import spray.http.ContentTypes._
import spray.http.HttpEntity
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._

class PermissionsSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures  with Matchers with MockitoSugar with CommonResults {
  val url = "/permissions"

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
    _: Seq[String],
    info = Some("some_info"),
    hash = Some("123456789"),
    sessionTime = Some(15)
  )

  "Permissions" should "be returned" in {

    val account = accountFactory(Seq("Permission_1, Permission_2"))

    when(accountRepository.findAccountByLogin(account.login)).thenReturn(someResult(account))

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}"}""")) ~> routs ~> check {
      responseAs[ResponseStringSeq] should be(ResponseStringSeq(200, Seq("Permission_1, Permission_2")))
    }
  }

  "Permissions" should "be ignored with 'This login doesn't match any account" in {

    val account = accountFactory(Seq("Permission_1, Permission_2"))

    when(accountRepository.findAccountByLogin(account.login)).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This login doesn't match any account"))
    }
  }

}
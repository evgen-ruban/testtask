package org.tests.accounts.spec

import org.accounts.model.{ResponseString, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.concurrent.ScalaFutures
import reactivemongo.bson.BSONObjectID
import spray.http.ContentTypes._
import spray.http.HttpEntity
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._


class ProfileUpdateInfoSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures with Matchers with MockitoSugar with CommonResults {
  val url = "/profile/update_info"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val accountFactory = Account(
    id = Some("my_id"),
    enabled = Some(true),
    _: Option[String],
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

  "Info" should "be updated" in {
    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))
    when(accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("123456789"))).thenReturn(someResult(account))
    when(accountRepository.updateAccountByLogin(account.copy(email = Some("some_email"), name = Some("some_name"), secondName = Some("some_secondName"), info = Some("newInfo")))).thenReturn(okUpdateWriteResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "hash": "${account.hash.get}", "email": "some_email", "name": "some_name", "secondName": "some_secondName", "info": "newInfo"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Info" should "be ignored with 'Wrong password'" in {
    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))
    when(accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("123456789"))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "hash": "${account.hash.get}", "email": "some_email", "name": "some_name", "secondName": "some_secondName", "info": "newInfo"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "Wrong password"))
    }
  }

  "Info" should "be ignored with 'This login doesn't match any document'" in {
    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "hash": "${account.hash.get}", "email": "some_email", "name": "some_name", "secondName": "some_secondName", "info": "newInfo"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This login doesn't match any document"))
    }
  }

}
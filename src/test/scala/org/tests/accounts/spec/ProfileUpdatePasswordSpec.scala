package org.tests.accounts.spec

import org.accounts.model.{ResponseString, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import reactivemongo.bson.BSONObjectID
import spray.http.ContentTypes._
import spray.http.HttpEntity
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._

class ProfileUpdatePasswordSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures  with Matchers with MockitoSugar with CommonResults {
  val url = "/profile/update_password"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val accountFactory = Account(
    id = Some("fjfjf"),
    enabled = Some(true),
    _ : Option[String],
    email = Some("ivanov@gmail.com"),
    name = Some("Ivan"),
    secondName = Some("Ivanov"),
    roles = Seq(),
    groups = Seq("5720a6bf56d06b2bbf907230"),
    permissions = Seq(),
    info = Some("some_info"),
    hash = Some("old_hash"),
    sessionTime = Some(15)
  )

  "Password by login" should "be updated" in {

    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))
    when(accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("old_hash"))).thenReturn(someResult(account))
    when(accountRepository.updatePasswordByLogin(Some("ivanov111"), Some("new_hash"))).thenReturn(okUpdateWriteResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "oldPassword": "old_hash", "newPassword": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Password by login" should "be ignored with 'Wrong password' message" in {

    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))
    when(accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("old_hash"))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "oldPassword": "old_hash", "newPassword": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "Wrong password"))
    }
  }

  "Password by login" should "be ignored with 'This login doesn't match any account' message" in {

    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "oldPassword": "old_hash", "newPassword": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This login doesn't match any account"))
    }
  }

  "Password by login" should "be ignored with 'Error' message" in {

    val account = accountFactory(Some("ivanov111"))

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))
    when(accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("old_hash"))).thenReturn(someResult(account))
    when(accountRepository.updatePasswordByLogin(Some("ivanov111"), Some("new_hash"))).thenReturn(exceptionResult)

    Post(url, HttpEntity(`application/json`, s"""{"login": "${account.login.get}", "oldPassword": "old_hash", "newPassword": "new_hash"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(400, "Error"))
    }
  }
}
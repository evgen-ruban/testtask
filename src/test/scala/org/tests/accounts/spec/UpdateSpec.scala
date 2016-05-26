package org.tests.accounts.spec

import org.accounts.model.{ResponseString, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.concurrent.ScalaFutures
import spray.http.StatusCodes
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._

class UpdateSpec extends FlatSpec with ScalatestRouteTest with Matchers with HttpService with AccountRoute with ScalaFutures with MockitoSugar with CommonResults {
  val url = "/update"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val account = Account(
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
    sessionTime = Some(15)
  )

  "Account" should "be updated" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)
    when(accountRepository.findAccountById(Some("my_id"))).thenReturn(someResult(account.copy(login = Some("OtherLogin"))))
    when(accountRepository.updateAccountById(account)).thenReturn(okUpdateWriteResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Account update" should "be ignored with 'Someone already has that login' message" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "Someone already has that login"))
    }
  }

  "Account update" should "be ignored with 'This id doesn't match any document' message" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)
    when(accountRepository.findAccountById(Some("my_id"))).thenReturn(noneResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This id doesn't match any document"))
    }
  }

}
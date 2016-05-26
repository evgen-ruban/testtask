package org.tests.accounts.spec

import org.accounts.model.{ResponseString, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.mockito.Mockito._
import org.accounts.model.CustomJsonProtocol._

class CreateSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures with Matchers with MockitoSugar with CommonResults {
  val url = "/create"

  def actorRefFactory = system

  override lazy val accountRepository = mock[AccountRepository]

  val account = Account(
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

  "Account" should "be created" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)

    when(accountRepository.create(account)).thenReturn(okWriteResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Account creation" should "be ignored with 'Someone already has that login' message" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(someResult(account))

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "Someone already has that login"))
    }
  }

  "Account creation" should "be ignored with 'Error' message" in {

    when(accountRepository.findAccountByLogin(Some("ivanov111"))).thenReturn(noneResult)

    when(accountRepository.create(account)).thenReturn(exceptionResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(400, "Error"))
    }
  }

}
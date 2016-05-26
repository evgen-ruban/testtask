package org.tests.accounts.spec

import org.accounts.model.{ResponseString, Account}
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import reactivemongo.bson.BSONObjectID
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.mockito.Mockito._
import org.accounts.model.CustomJsonProtocol._

class EnableSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures with Matchers with MockitoSugar with CommonResults {
  val url = "/enable"
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

  "Account" should "be enable" in {
    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(someResult(account))

    when(accountRepository.enableAccount(account)).thenReturn(okUpdateWriteResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Account" should "be ignored with 'This id doesn't match any document' message" in {
    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(noneResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This id doesn't match any document"))
    }
  }

  "Account" should "not be enable with 'Error' message" in {
    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(someResult(account))

    when(accountRepository.enableAccount(account)).thenReturn(exceptionResult)

    Post(url, account) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(400, "Error"))
    }
  }
}
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

class DeleteGroupFromAccountSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures  with Matchers with MockitoSugar with CommonResults {
  def actorRefFactory = system
  val url = "/delete_group"

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

  "Group" should "be deleted" in {

    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(someResult(account))

    when(accountRepository.updateAccountById(account.copy(groups = account.groups.filter(_  != Some("some_groupId"))))).thenReturn(okUpdateWriteResult)

    Post(url, HttpEntity(`application/json`, s"""{"id": "$id", "idGroup": "some_groupId"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(200, "Success"))
    }
  }

  "Group" should "be ignored with 'This id doesn't match any document' message" in {

    val id = BSONObjectID.generate.stringify

    when(accountRepository.findAccountById(Some(id))).thenReturn(noneResult)

    Post(url, HttpEntity(`application/json`, s"""{"id": "$id", "idGroup": "some_groupId"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(404, "This id doesn't match any document"))
    }
  }

  "Group" should "not be deleted with 'Error' message " in {

    val id = BSONObjectID.generate.stringify
    val account = accountFactory(Some(id))

    when(accountRepository.findAccountById(Some(id))).thenReturn(someResult(account))

    when(accountRepository.updateAccountById(account.copy(groups = account.groups.filter(_  != Some("some_groupId"))))).thenReturn(exceptionResult)

    Post(url, HttpEntity(`application/json`, s"""{"id": "$id", "idGroup": "some_groupId"}""")) ~> routs ~> check {
      responseAs[ResponseString] should be(ResponseString(400, "Error"))
    }
  }
}
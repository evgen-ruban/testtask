package org.tests.accounts.spec

import org.accounts.model._
import org.accounts.repository.AccountRepository
import org.accounts.routes.AccountRoute
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import reactivemongo.core.errors.GenericDriverException
import spray.http.StatusCodes
import spray.routing.HttpService
import spray.testkit.ScalatestRouteTest
import org.accounts.model.CustomJsonProtocol._

import scala.concurrent.Future

class ListSpec extends FlatSpec with ScalatestRouteTest with HttpService with AccountRoute with ScalaFutures with Matchers with MockitoSugar with CommonResults {
  val url = "/list"

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

  "Request to list" should "return 2 accounts" in {
    val accounts = List(accountFactory(Some("1")), accountFactory(Some("2")))

    when(accountRepository.findAll()).thenReturn(Future(accounts))

    Post(url) ~> routs ~> check {
      responseAs[ResponseAccountSeq] should be(ResponseAccountSeq(200, accounts))
    }
  }
}
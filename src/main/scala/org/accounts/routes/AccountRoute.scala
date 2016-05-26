package org.accounts.routes


import org.accounts.MongoDBConnection
import org.accounts.model.Account
import org.accounts.model.CustomJsonProtocol._
import org.accounts.repository.AccountRepository
import org.accounts.service.AccountService
import spray.json.{JsValue, pimpString}
import spray.routing.{HttpService, RequestContext}

trait AccountRoute extends HttpService {

  lazy val accountRepository = new AccountRepository(MongoDBConnection.collection_accounts)

  lazy val accountService = new AccountService(accountRepository)

  val routs =
    post {
      path("create") {
        entity(as[Account]) { account =>
          implicit requestContext =>
            accountService.createAccount(account)
        }
      } ~
        path("update") {
          entity(as[Account]) { account =>
            implicit requestContext =>
              accountService.updateAccount(account)
          }
        } ~
        path("update_password") {
          withJson { json =>
            implicit requestContext =>
              val accountId = json.getString("id")
              val hash = json.getString("hash")
              accountService.updatePassword(accountId, hash)
          }
        } ~
        path("profile" / "update_password") {
          withJson { json =>
            implicit requestContext =>
              val login = json.getString("login")
              val oldPassword = json.getString("oldPassword")
              val newPassword = json.getString("newPassword")
              accountService.updatePasswordByUser(login, oldPassword, newPassword)
          }
        } ~
        path("profile" / "update_info") {
          withJson { json =>
            implicit requestContext =>
              val login = json.getString("login")
              val hash = json.getString("hash")
              val email = json.getString("email")
              val name = json.getString("name")
              val secondName = json.getString("secondName")
              val info = json.getString("info")
              accountService.updateInfoByLogin(login, hash, email, name, secondName, info)
          }
        } ~
        path("list") {
          implicit requestContext =>
            accountService.listAccount
        } ~
        path("enable") {
          entity(as[Account]) { account =>
            implicit requestContext =>
              accountService.enableAccount(account)
          }
        } ~
        path("disable") {
          entity(as[Account]) { account =>
            implicit requestContext =>
              accountService.disableAccount(account)
          }
        } ~
        path("delete_group") {
          withJson { json =>
            implicit requestContext =>
              val idAccount = json.getString("id")
              val idGroup = json.getString("idGroup")
              accountService.deleteAccountFromGroup(idAccount, idGroup)
          }
        } ~
        path("delete_role") {
          withJson { json =>
            implicit requestContext =>
              val idAccount = json.getString("id")
              val idRole = json.getString("idRole")
              accountService.deleteRoleFromAccount(idAccount, idRole)
          }
        } ~
        path("insert_role") {
          withJson { json =>
            implicit requestContext =>
              val idAccount = json.getString("id")
              val idRole = json.getString("idRole")
              accountService.addRoleToAccount(idAccount, idRole)
          }
        } ~
        path("session_time") {
            withJson { json =>
              implicit requestContext =>
                val login = json.getString("login")
                accountService.sessionTime(login)
            }
        } ~
        path("permissions") {
          withJson { json =>
            implicit requestContext =>
              val login = json.getString("login")
              accountService.listPermissions(login)
          }
        }
    }

  def withJson = extract { body =>
    new SimpleJson(body.request.entity.asString.parseJson)
  }

  class SimpleJson(jsValue: JsValue) {
    def getString(field: String) = jsValue.asJsObject.fields(field).convertTo[String]
  }

}

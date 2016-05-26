package org.accounts.service

import org.accounts._
import org.accounts.model._
import org.accounts.repository.AccountRepository

import spray.http.StatusCodes
import spray.routing.RequestContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Try}
import org.accounts.model.CustomJsonProtocol._


class AccountService(accountRepository: AccountRepository) {

  def createAccount(account: Account)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(account.login).map {
      case None =>
        accountRepository.create(account).onComplete(processFinalResult)
      case _ =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "Someone already has that login"))
    }
  }

  def updateAccount(account: Account)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(account.login).map {
      case None =>
        accountRepository.findAccountById(account.id).map {
          case None =>
            requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"))
          case _ =>
            accountRepository.updateAccountById(account).onComplete(processFinalResult)
        }
      case _ =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "Someone already has that login"))
    }
  }

  def updatePassword(accountId: String, hash: String)(implicit requestContext: RequestContext): Unit = {
    withAccountId(accountId) { account =>
      val updatedAccount = account.copy(hash = Some(hash))
      accountRepository.updateAccountById(updatedAccount).onComplete(processFinalResult)
    }
  }

  def updatePasswordByUser(login: String, oldPassword: String, newPassword: String)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(Some(login)).map {
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This login doesn't match any account"))
      case _ =>
        accountRepository.checkPasswordByLogin(Some(login), Some(oldPassword)).map {
          case None =>
            requestContext.complete(ResponseString(StatusCodes.NotFound, "Wrong password"))
          case _ =>
            accountRepository.updatePasswordByLogin(Some(login), Some(newPassword)).onComplete(processFinalResult)
        }
    }
  }

  def updateInfoByLogin(login: String, hash: String, em: String, nam: String, secondN: String, inf: String)(implicit requestContext: RequestContext): Unit = {
    withAccountLogin(login) { findAccount =>
      accountRepository.checkPasswordByLogin(Some(login), Some(hash)).map {
        case None =>
          requestContext.complete(ResponseString(StatusCodes.NotFound, "Wrong password"))
        case _ =>
          val updatedAccount = findAccount.copy(email = Option(em), name = Option(nam), secondName = Option(secondN), info = Option(inf))
          accountRepository.updateAccountByLogin(updatedAccount).onComplete(processFinalResult)
      }
    }
  }

  def listAccount(implicit requestContext: RequestContext): Unit = {
    val accountsFuture =  accountRepository.findAll()
    val response = accountsFuture.map(accounts => ResponseAccountSeq(StatusCodes.OK, accounts))
    requestContext.complete(response)
  }

  def enableAccount(account: Account)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountById(account.id).map {
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"));
      case _ =>
        accountRepository.enableAccount(account).onComplete(processFinalResult)
    }
  }

  def disableAccount(account: Account)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountById(account.id).map {
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"));
      case _ =>
        accountRepository.disableAccount(account).onComplete(processFinalResult)
    }
  }

  def deleteAccountFromGroup(accountId: String, groupId: String)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountById(Some(accountId)).map {
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"))
      case Some(account) =>
        val updatedAccount = account.copy(groups = account.groups.filter(_  != groupId))
        accountRepository.updateAccountById(updatedAccount).onComplete(processFinalResult)
    }
  }

  def deleteRoleFromAccount(accountId: String, roleId: String)(implicit requestContext: RequestContext): Unit = {
    withAccountId(accountId) { account =>
      val updatedAccount = account.copy(roles = account.roles.filter(_  != roleId))
      accountRepository.updateAccountById(updatedAccount).onComplete(processFinalResult)
    }
  }

  def addRoleToAccount(accountId: String, roleId: String)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountById(Some(accountId)).map {
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"))
      case Some(account) =>
        val updatedAccount = account.copy(roles = account.roles :+ roleId)
        accountRepository.updateAccountById(updatedAccount).onComplete(processFinalResult)
    }
  }

  def sessionTime(login: String)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(Some(login)).map {
      case Some(account) =>
        account.sessionTime match {
          case Some(sessionTime) => requestContext.complete(ResponseInt(StatusCodes.OK, sessionTime))
          case None => requestContext.complete(ResponseString(StatusCodes.BadRequest, "Error"))
        }
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This login doesn't match any account"))
    }
  }

  def listPermissions(login: String)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(Some(login)).map {
      case Some(account) =>
        account.permissions match {
          case permissions => requestContext.complete(ResponseStringSeq(StatusCodes.OK, permissions))
        }
      case None =>
        requestContext.complete(ResponseString(StatusCodes.NotFound, "This login doesn't match any account"))
    }
  }

  def withAccountId(accountId: String)(fun: (Account) => Unit)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountById(Some(accountId)).map {
      case None => requestContext.complete(ResponseString(StatusCodes.NotFound, "This id doesn't match any document"))
      case Some(account) => fun(account)
    }
  }

  def withAccountLogin(login: String)(fun: (Account) => Unit)(implicit requestContext: RequestContext): Unit = {
    accountRepository.findAccountByLogin(Some(login)).map {
      case None => requestContext.complete(ResponseString(StatusCodes.NotFound, "This login doesn't match any document"))
      case Some(account) => fun(account)
    }
  }

  def processFinalResult[T](value: Try[T])(implicit requestContext: RequestContext): Unit = {
    value match {
      case Failure(ex) => requestContext.complete(ResponseString(StatusCodes.BadRequest, "Error"))
      case _ => requestContext.complete(ResponseString(StatusCodes.OK, "Success"))
    }
  }
}
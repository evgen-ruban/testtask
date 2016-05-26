package org.accounts.repository

import org.accounts.model.Account
import org.accounts.model.AccountEntity._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONDocument}

import scala.concurrent.ExecutionContext.Implicits.global

class AccountRepository(accountCollection: BSONCollection) {

  def updateAccountById(account: Account) = {
    accountCollection.update(BSONDocument("_id" -> account.id.map(BSONObjectID(_))), BSONDocument("$set" -> account))
  }

  def updateAccountByLogin(account: Account) = {
    accountCollection.update(BSONDocument("login" -> account.login), BSONDocument("$set" -> account))
  }

  def findAccountById(id: Option[String]) = {
    accountCollection.find(BSONDocument("_id" -> id.map(BSONObjectID(_)))).one[Account]
  }

  def findAccountByLogin(login: Option[String]) = {
    accountCollection.find(BSONDocument("login" -> login)).one[Account]
  }

  def checkPasswordByLogin(login: Option[String], hash: Option[String]) =  {
    accountCollection.find(BSONDocument("login" -> login, "hash" -> hash)).one[Account]
  }

  def updatePasswordByLogin(login: Option[String], newPassword: Option[String]) = {
    val selector = BSONDocument("login" -> login)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "hash" -> newPassword))
    accountCollection.update(selector, modifier)
  }

  def removeAll() = {
    accountCollection.remove(BSONDocument.empty)
  }

  def findAll() = {
    accountCollection.find(BSONDocument.empty).cursor[Account]().collect[List]()
  }

  def create(account: Account) = {
    accountCollection.insert(account)
  }


  def enableAccount(account: Account) = {
    val selector = BSONDocument("_id" -> account.id)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "enable" -> true))
    accountCollection.update(selector, modifier)
  }

  def disableAccount(account: Account) = {
    val selector = BSONDocument("_id" -> account.id)
    val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "enable" -> false))
    accountCollection.update(selector, modifier)
  }
}

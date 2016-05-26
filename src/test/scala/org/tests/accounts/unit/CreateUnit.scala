package org.tests.accounts.unit

import org.accounts.MongoDBConnection.collection_accounts
import org.accounts.model.Account
import org.accounts.repository.AccountRepository
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

class CreateUnit extends FunSuite with ScalaFutures {

 /* val account = Account(
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
  val accountRepository = new AccountRepository(collection_accounts)

  accountRepository.create(account)
  assert(account.email.get == accountRepository.checkPasswordByLogin(Some("ivanov111"), Some("123456789")).value.get.get.get.email.get)*/
}
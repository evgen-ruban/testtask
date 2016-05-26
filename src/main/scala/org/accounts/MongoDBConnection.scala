package org.accounts

import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection
import scala.concurrent.ExecutionContext.Implicits.global

import Config.MongoDB._

object MongoDBConnection {
  val driver = new MongoDriver
  val connection = driver.connection(List(mongoHost))
  val db = connection(dbName)
  val collection_accounts: BSONCollection = db(collectionAccounts)

  def init(): Unit = {}
}

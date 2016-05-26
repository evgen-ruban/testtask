package org.accounts

import com.typesafe.config.ConfigFactory

object Config {
  val configFactory = ConfigFactory.load().getConfig("app")
  // App configs
  val serviceName = configFactory.getString("serviceName")
  val timeoutService = configFactory.getInt("timeout")
  val port = configFactory.getInt("port")
  val interface = configFactory.getString("interface")
  val actorSystemName = configFactory.getString("actorSystemName")

  //version of service
  val version = configFactory.getString("version")

  object MongoDB {
    val mongoConfig = configFactory.getConfig("mongo")
    val mongoHost = mongoConfig.getString("mongoHost")
    val dbName = mongoConfig.getString("dbName")
    val collectionAccounts = mongoConfig.getString("collectionAccounts")
  }
}
package org.accounts.model

import reactivemongo.bson.{BSONObjectID, BSONDocument, BSONDocumentReader, BSONDocumentWriter}

  /**
  * Account
  * {""enabled"":true,
  * ""suspended"":false,   ????
  * ""login"":""ivanov111"",
  * ""email"":""ivanov@gmail.com"",
  * ""name"":""Ivan"",
  * ""secondName"":""Ivanov"",
  * ""accountType"":""user"", ????
  * ""roles"":[],
  * ""groups"":[""5720a6bf56d06b2bbf907230""],
  * ""permissions"":[],
  * ""info"":""some info"",
  * ""hash"":""123456789"",
  * ""sessionTime"":15}"
  * "created": DateTime = DateTime.now.withZone(DateTimeZone.UTC) ????
  */

case class Account(id: Option[String] = None, enabled: Option[Boolean], login: Option[String], email: Option[String], name: Option[String],
                   secondName: Option[String], roles: Seq[String], groups: Seq[String], permissions: Seq[String], info: Option[String],
                   hash: Option[String], sessionTime: Option[Int]){
}

object AccountEntity {

    implicit object AccountEntityBSONReader extends BSONDocumentReader[Account] {

      def read(doc: BSONDocument): Account =
        Account(
          id = doc.getAs[BSONObjectID]("_id").map(_.stringify),
          enabled = doc.getAs[Boolean]("enabled"),
          login = doc.getAs[String]("login"),
          email = doc.getAs[String]("email"),
          name = doc.getAs[String]("name"),
          secondName = doc.getAs[String]("surname"),
          roles = doc.getAs[Seq[String]]("roles").getOrElse(Seq()),
          groups = doc.getAs[Seq[String]]("groups").getOrElse(Seq()),
          permissions = doc.getAs[Seq[String]]("permissions").getOrElse(Seq()),
          info = doc.getAs[String]("info"),
          hash = doc.getAs[String]("hash"),
          sessionTime = doc.getAs[Int]("session_time")
        )
    }

    implicit object AccountEntityBSONWriter extends BSONDocumentWriter[Account] {
      def write(account: Account): BSONDocument =
        BSONDocument(
          "_id" -> account.id.map(BSONObjectID(_)),
          "enabled" -> account.enabled,
          "login" -> account.login,
          "email" -> account.email,
          "name" -> account.name,
          "surname" -> account.secondName,
          "roles" -> account.roles,
          "groups" -> account.groups,
          "permissions" -> account.permissions,
          "info" -> account.info,
          "hash" -> account.hash,
          "session_time" -> account.sessionTime
        )
    }
}



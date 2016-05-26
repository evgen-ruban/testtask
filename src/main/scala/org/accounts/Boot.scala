package org.accounts

import scala.concurrent.duration._
import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.accounts.routes.MainActor
import spray.can.Http

import Config._

object Boot extends App {
  implicit val system = ActorSystem(actorSystemName)
  val service = system.actorOf(Props[MainActor], serviceName)
  implicit val timeout = Timeout(timeoutService.seconds)
  IO(Http) ? Http.Bind(service, interface = interface, port = port)

  MongoDBConnection.init()
}
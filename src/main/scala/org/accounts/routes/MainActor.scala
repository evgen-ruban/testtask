package org.accounts.routes

import akka.actor.Actor

class MainActor extends Actor with AccountRoute {
  def actorRefFactory = context
  def receive = runRoute(routs)
}



package org.tests.accounts.spec

import reactivemongo.api.commands.{DefaultWriteResult, UpdateWriteResult}
import reactivemongo.core.errors.GenericDriverException

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait CommonResults {
  val okUpdateWriteResult = Future(UpdateWriteResult(ok = true, 1, 1, Nil, Nil, None, None, None))
  val okWriteResult = Future(DefaultWriteResult(ok = true, 1, Nil, None, None, None))
  val exceptionResult = Future.failed(GenericDriverException("Exception"))
  val noneResult = Future(None)
  def someResult[T](value: T) = Future(Some(value))
}

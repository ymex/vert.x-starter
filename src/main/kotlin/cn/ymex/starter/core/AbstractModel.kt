package cn.ymex.starter.core

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

abstract class AbstractModel<T, R> : Model<T, R> {
  override fun send(vertx: Vertx, body: T, replyHandler: Handler<AsyncResult<Message<R>>>) {
    vertx.eventBus().send<R>(javaClass.name, body, replyHandler)
  }
}

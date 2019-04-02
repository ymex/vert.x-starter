package cn.ymex.starter.core

import io.reactiverse.pgclient.PgPool
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

interface Model<T, R> {
  fun send(vertx: Vertx, body: T, replyHandler: Handler<AsyncResult<Message<R>>>)
  fun reply(pg: PgPool): Handler<Message<T>>
}

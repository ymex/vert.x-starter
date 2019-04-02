package cn.ymex.starter.model

import io.reactiverse.pgclient.PgPool
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message

interface Model<T,R> {
  fun  send(body: T, replyHandler: Handler<AsyncResult<Message<R>>>)
  fun  reply(pg: PgPool) :Handler<Message<T>>
}

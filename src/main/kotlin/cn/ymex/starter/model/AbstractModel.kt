package cn.ymex.starter.model

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message

abstract class AbstractModel<T,R> :Model<T,R>{
  override fun send(body: T, replyHandler: Handler<AsyncResult<Message<R>>>) {
    
  }
}

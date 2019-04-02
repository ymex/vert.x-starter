package cn.ymex.starter.model

import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class ScoreModel(val vertx: Vertx):Model<JsonObject,String>{
  override fun send(body: JsonObject, replyHandler: Handler<AsyncResult<Message<String>>>) {
    vertx.eventBus().send<String>(javaClass.name, body, replyHandler)
  }

  override fun reply(pg: PgPool): Handler<Message<JsonObject>> {
    return Handler {
      pg.preparedQuery(
        "insert into member(name, age) values ($1,$2) returning id;",
        Tuple.of(it.body().getString("name"), it.body().getString("age").toInt())
      ) { ar ->
        if (ar.succeeded()) {
          val id = ar.result().iterator().next().getInteger("id")
          it.reply(id.toString())
        } else {
          it.fail(500, ar.cause().message)
        }
      }
    }
  }
}

package cn.ymex.starter.model

import cn.ymex.starter.core.AbstractModel
import cn.ymex.starter.core.annotation.Model
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.Tuple
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

@Model
class ScoreModel : AbstractModel<JsonObject, String>() {
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

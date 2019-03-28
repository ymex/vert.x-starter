package cn.ymex.starter.router

import cn.ymex.starter.jsonContentType
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.Tuple
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class ScoreHandler(val db: PgPool) : Handler<RoutingContext> {

  private val logger = LoggerFactory.getLogger(ScoreHandler::class.java)

  override fun handle(it: RoutingContext) {
    if (it.bodyAsJson == null) {
      it.response().jsonContentType().end("{}")
      return
    }

    var body = it.bodyAsJson

    db.preparedQuery(
      "insert into member(name, age) values ($1,$2) returning id;",
      Tuple.of(body.getString("name"), body.getString("age").toInt())
    ) { ar ->
      if (ar.succeeded()) {
        val id = ar.result().iterator().next().getInteger("id")
        body.put("uid", id)
        it.response().jsonContentType().end(body.toString())
      } else {
        logger.info(ar.cause().toString())
      }
    }

  }
}

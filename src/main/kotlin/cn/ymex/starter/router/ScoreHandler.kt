package cn.ymex.starter.router

import cn.ymex.starter.core.ext.jsonContentType
import cn.ymex.starter.main.DataSource
import io.reactiverse.pgclient.Tuple
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class ScoreHandler(val vertx: Vertx) : Handler<RoutingContext> {

  private val logger = LoggerFactory.getLogger(ScoreHandler::class.java)

  override fun handle(context: RoutingContext) {
    if (context.bodyAsJson == null) {
      context.response().jsonContentType().end("{}")
      return
    }
    val body = context.bodyAsJson


    DataSource.db().preparedQuery(
      "insert into member(name, age) values ($1,$2) returning id;",
      Tuple.of(body.getString("name"), body.getString("age").toInt())
    ) { ar ->
      if (ar.succeeded()) {
        val id = ar.result().iterator().next().getInteger("id")
        context.response().jsonContentType().end(body.toString())
      } else {
        context.fail(500, ar.cause())
      }
    }
  }
}


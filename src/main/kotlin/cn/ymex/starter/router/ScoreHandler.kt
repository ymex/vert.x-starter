package cn.ymex.starter.router

import cn.ymex.starter.core.ext.jsonContentType
import cn.ymex.starter.model.ScoreModel
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

class ScoreHandler(val vertx: Vertx) : Handler<RoutingContext> {

  override fun handle(context: RoutingContext) {
    if (context.bodyAsJson == null) {
      context.response().jsonContentType().end("{}")
      return
    }
    val body = context.bodyAsJson
    ScoreModel().addMember(body.getString("name"), body.getString("age").toInt(), Handler { ar ->
      if (ar.succeeded()) {
        val id = ar.result().iterator().next().getInteger("id")
        body.put("id", id)
        context.response().jsonContentType().end(body.toString())
      } else {
        context.fail(500, ar.cause())
      }
    })
  }
}


package cn.ymex.starter.router

import cn.ymex.starter.core.ext.jsonContentType
import cn.ymex.starter.model.ScoreModel
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

    ScoreModel().send(vertx, body, Handler {
      if (it.succeeded()) {
        body.put("uid", it.result().body())
        context.response().jsonContentType().end(body.toString())
      } else {
        context.fail(500, it.cause())
      }
    })
  }
}


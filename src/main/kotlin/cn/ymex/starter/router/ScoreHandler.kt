package cn.ymex.starter.router

import cn.ymex.starter.kits.jsonContentType
import cn.ymex.starter.model.ModelRegister
import cn.ymex.starter.model.ScoreModel
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
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

//    vertx.eventBus().send<String>("add_score",body){
//
//    }
     ScoreModel(vertx).send(body, Handler {
       if (it.succeeded()) {
          body.put("uid", it.result().body())
          context.response().jsonContentType().end(body.toString())
        }else{
          context.fail(500, it.cause())
        }
     })
//    ModelRegister<String>(vertx).send(body,
//      Handler {
//        if (it.succeeded()) {
//          body.put("uid", it.result().body())
//          context.response().jsonContentType().end(body.toString())
//        }else{
//          context.fail(500, it.cause())
//        }
//      })
  }
}


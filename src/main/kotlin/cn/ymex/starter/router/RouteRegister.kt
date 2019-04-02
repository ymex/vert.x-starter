package cn.ymex.starter.router

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CookieHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import org.slf4j.LoggerFactory

class RouteRegister(val vertx: Vertx) {
  private val logger = LoggerFactory.getLogger(RouteRegister::class.java)

  fun handler(): Router {

    val r = Router.router(vertx)

    r.route().handler(LoggerHandler(logger))
    r.route().handler(CookieHandler.create())
    r.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

    r.route("/static/*").handler(StaticHandler.create())
    r.route("/").handler(IndexHandler())
    r.post("/score").handler(BodyHandler.create()).handler(ScoreHandler(vertx))

    return r
  }
}

package cn.ymex.starter.router

import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext
import org.slf4j.Logger

class LoggerHandler(val logger: Logger) : Handler<RoutingContext> {
  override fun handle(event: RoutingContext) {
    logger.info(event.request().uri())
    event.next()
  }

}


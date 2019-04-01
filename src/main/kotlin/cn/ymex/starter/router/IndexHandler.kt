package cn.ymex.starter.router

import cn.ymex.starter.kits.textContentType
import io.vertx.core.Handler
import io.vertx.ext.web.RoutingContext

class IndexHandler : Handler<RoutingContext> {

  override fun handle(event: RoutingContext) {
    event.response()
      .textContentType()
      .end("Hello from Vert.x!")
  }
}

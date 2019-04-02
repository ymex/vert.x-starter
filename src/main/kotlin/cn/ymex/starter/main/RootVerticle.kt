package cn.ymex.starter.main

import cn.ymex.starter.router.RouteRegister
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import org.slf4j.bridge.SLF4JBridgeHandler


class RootVerticle : AbstractVerticle() {
  init {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

  override fun start(startFuture: Future<Void>) {

    vertx.deployVerticle(DataVerticle::class.java.name,
      DeploymentOptions().setInstances(4))

    vertx.createHttpServer().requestHandler(RouteRegister(vertx).handler())
      .listen(8888) { http ->
        if (http.succeeded()) {
          startFuture.complete()
          println("HTTP server started on port 8888")
        } else {
          startFuture.fail(http.cause())
        }
      }

  }

}

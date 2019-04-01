package cn.ymex.starter.main


import cn.ymex.starter.kits.toBean
import cn.ymex.starter.router.IndexHandler
import cn.ymex.starter.router.ScoreHandler
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler


class RootVerticle : AbstractVerticle() {
  init {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

  private val logger = LoggerFactory.getLogger(RootVerticle::class.java)

  override fun start(startFuture: Future<Void>) {


    val appRouter = Router.router(vertx)
    appRouter.route("/").handler(IndexHandler())
    appRouter.post("/score").handler(BodyHandler.create()).handler(ScoreHandler(vertx))

    vertx.deployVerticle(DataBaseVerticle::class.java.name, DeploymentOptions().setInstances(4))

    vertx.createHttpServer().requestHandler(appRouter)
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

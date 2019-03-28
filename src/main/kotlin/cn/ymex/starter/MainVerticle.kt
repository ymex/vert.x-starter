package cn.ymex.starter


import cn.ymex.starter.router.IndexHandler
import cn.ymex.starter.router.ScoreHandler
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.slf4j.bridge.SLF4JBridgeHandler


class MainVerticle : AbstractVerticle() {
  init {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

  override fun start(startFuture: Future<Void>) {
    config()
    val options = PgPoolOptions()
      .setPort(5432)
      .setHost("127.0.0.1")
      .setDatabase("ForL")
      .setUser("postgres")
      .setPassword("ymex")
      .setMaxSize(5)
    val client = PgClient.pool(vertx, options)
    val appRouter = Router.router(vertx)

    appRouter.route("/").handler(IndexHandler())
    appRouter.post("/score").handler(BodyHandler.create()).handler(ScoreHandler(client))

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

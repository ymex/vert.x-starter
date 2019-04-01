package cn.ymex.starter


import cn.ymex.starter.router.IndexHandler
import cn.ymex.starter.router.ScoreHandler
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler


class MainVerticle : AbstractVerticle() {
  init {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

  private val logger = LoggerFactory.getLogger(MainVerticle::class.java)

  override fun start(startFuture: Future<Void>) {

    val dbconfig = config()

    //vert.x 文件系统api
    vertx.fileSystem().readFile("E:/Workspace/jetbrains/vert.starter/src/main/resources/postgredb.json"){
      logger.info(it.result().toBean())
    }

    val options = PgPoolOptions()
      .setPort(dbconfig.getInteger("port"))
      .setHost(dbconfig.getString("host"))
      .setDatabase(dbconfig.getString("database"))
      .setUser(dbconfig.getString("user"))
      .setPassword(dbconfig.getString("password"))
      .setMaxSize(dbconfig.getInteger("pool_size",10))
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

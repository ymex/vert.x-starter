package cn.ymex.starter.main


import cn.ymex.starter.kits.toBean
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


class RootVerticle : AbstractVerticle() {
  init {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
  }

  private val logger = LoggerFactory.getLogger(RootVerticle::class.java)

  override fun start(startFuture: Future<Void>) {

    val dbconf = vertx.fileSystem().readFileBlocking("postgredb.json").toBean<DBConf>()

    //vert.x 文件系统api
//    vertx.fileSystem().readFile("postgredb.json") {
//      val conf = it.result().toBean<DbConfig>()
//      logger.info(conf.host)
//    }
    logger.info(dbconf.host)


    val options = PgPoolOptions()
      .setPort(dbconf.port)
      .setHost(dbconf.host)
      .setDatabase(dbconf.database)
      .setUser(dbconf.user)
      .setPassword(dbconf.password)
      .setMaxSize(dbconf.poolSize)
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

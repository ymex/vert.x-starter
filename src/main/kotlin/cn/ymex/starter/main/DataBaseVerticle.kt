package cn.ymex.starter.main

import cn.ymex.starter.kits.toBean
import cn.ymex.starter.model.ScoreModel
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.AbstractVerticle

class DataBaseVerticle : AbstractVerticle() {
  //数据库链接池
  lateinit var pg: PgPool

  override fun start() {
    super.start()
    val dbconf = vertx.fileSystem().readFileBlocking("postgredb.json").toBean<DBConf>()

    val options = PgPoolOptions()
      .setPort(dbconf.port)
      .setHost(dbconf.host)
      .setDatabase(dbconf.database)
      .setUser(dbconf.user)
      .setPassword(dbconf.password)
      .setMaxSize(dbconf.poolSize)
    pg = PgClient.pool(vertx, options)
    vertx.eventBus().consumer(ScoreModel::class.java.name, ScoreModel(vertx).reply(pg))
  }


  override fun stop() {
    super.stop()
    pg.close()
  }
}

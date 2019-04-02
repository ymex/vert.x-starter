package cn.ymex.starter.main

import cn.ymex.starter.core.PathScan
import cn.ymex.starter.core.ext.toBean
import cn.ymex.starter.model.ModelRegister
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message

class DataVerticle : AbstractVerticle() {
  //数据库链接池
  var pg: PgPool? = null

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

    pg?.run {
      ModelRegister.models.forEach {
        vertx.eventBus().consumer(it.javaClass.name, it.reply(this) as Handler<Message<Any>>?)
      }
    }
  }


  override fun stop() {
    super.stop()
    pg?.close()
  }
}

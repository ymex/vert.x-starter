package cn.ymex.starter.main

import cn.ymex.starter.kits.toBean
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPoolOptions
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

class DataBaseVerticle : AbstractVerticle() {
  //数据库链接池
  lateinit var dbClient: PgClient

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
    dbClient= PgClient.pool(vertx, options)
    vertx.eventBus().consumer<JsonObject>("add_score") {

      dbClient.preparedQuery(
        "insert into member(name, age) values ($1,$2) returning id;",
        Tuple.of(it.body().getString("name"), it.body().getString("age").toInt())
      ) { ar ->
        if (ar.succeeded()) {
          val id = ar.result().iterator().next().getInteger("id")
          it.reply(id.toString())
        } else {
          it.fail(500, ar.cause().message)
        }
      }
    }
  }
}

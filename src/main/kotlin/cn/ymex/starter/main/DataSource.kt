package cn.ymex.starter.main

import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.Vertx
import org.slf4j.LoggerFactory

object DataSource {
  //数据库链接池
  val L = LoggerFactory.getLogger(DataSource::class.java)
  private var pg: PgPool? = null

  fun getSource(vertx: Vertx, dbconf: DBConf): PgPool {
    L.info("dbsource:{} db=>{}",dbconf.host,dbconf.database)
    val options = PgPoolOptions()
      .setPort(dbconf.port)
      .setHost(dbconf.host)
      .setDatabase(dbconf.database)
      .setUser(dbconf.user)
      .setPassword(dbconf.password)
      .setMaxSize(dbconf.poolSize)
    pg = PgClient.pool(vertx, options)
    return pg!!
  }

  fun close() {
    pg?.run {
      this.close()
    }
  }
}

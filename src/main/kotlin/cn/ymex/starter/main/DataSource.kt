package cn.ymex.starter.main

import cn.ymex.starter.core.ext.toBean
import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.Vertx
import org.slf4j.LoggerFactory

object DataSource {



  //数据库链接池
  val L = LoggerFactory.getLogger(DataSource::class.java)
  private var pg: PgPool? = null

  fun db(): PgPool {
    if (pg == null) {
      throw RuntimeException("无法获取数据库连接！")
    }
    return pg!!
  }

  fun getSource(vertx: Vertx): PgPool {
    val dbconf = vertx.fileSystem().readFileBlocking("postgredb.json").toBean<DBConf>()
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

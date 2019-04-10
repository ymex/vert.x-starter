package cn.ymex.starter.model

import cn.ymex.starter.main.DataSource
import io.reactiverse.pgclient.PgPool

open class Model {
  var db: PgPool = DataSource.db()
}

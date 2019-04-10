package cn.ymex.starter.model

import io.reactiverse.pgclient.PgRowSet
import io.reactiverse.pgclient.Tuple
import io.vertx.core.AsyncResult
import io.vertx.core.Handler


class ScoreModel : Model() {

  //添加
  fun addMember(name: String, age: Int, handler: Handler<AsyncResult<PgRowSet>>) {
    db.preparedQuery(
      "insert into member(name, age) values ($1,$2) returning id;",
      Tuple.of(name, age)
      , handler
    )
  }
}

package cn.ymex.starter.main

import io.vertx.core.AbstractVerticle

class DataVerticle : AbstractVerticle() {

  override fun start() {
    super.start()
    DataSource.getSource(vertx)
  }


  override fun stop() {
    super.stop()
    DataSource.close()
  }
}

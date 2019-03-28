package cn.ymex.starter

import io.vertx.core.Launcher
import io.vertx.core.VertxOptions

fun main(args: Array<String>) {
  App().dispatch(args)
}


open class App : Launcher() {


  override fun beforeStartingVertx(options: VertxOptions?) {
    options?.run {
      warningExceptionTime = 10 * 1000 * 1000000L
      blockedThreadCheckInterval = 2000L
      maxEventLoopExecuteTime = 2 * 1000 * 1000000L
    }
    super.beforeStartingVertx(options)
  }
}

package cn.ymex.starter.main

import cn.ymex.starter.kits.getFileResourcePath


object App {
  @JvmStatic
  fun main(args: Array<String>) {
    val conf = mutableListOf<String>()
    if (!args.contains("run")) {
      conf.add("run")
      conf.add("cn.ymex.starter.main.RootVerticle")
    }
//    if (!args.contains("-conf")) {
//      conf.add("-conf")
//      println("---------file:  " + App::class.java.classLoader.getResource("/postgredb.json"))
//      conf.add(getFileResourcePath<App>("/postgredb.json"))
//    }
    conf.addAll(args)
    AppLauncher().dispatch(conf.toTypedArray())
  }
}


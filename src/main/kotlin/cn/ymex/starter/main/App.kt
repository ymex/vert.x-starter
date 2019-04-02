package cn.ymex.starter.main

import cn.ymex.starter.core.AbstractModel
import cn.ymex.starter.core.PathScan
import cn.ymex.starter.core.annotation.Model
import cn.ymex.starter.model.ModelRegister


object App {
  @JvmStatic
  fun main(args: Array<String>) {
    val conf = mutableListOf<String>()
    if (!args.contains("run")) {
      conf.add("run")
      conf.add("cn.ymex.starter.main.RootVerticle")
    }
    conf.addAll(args)
    PathScan.scanClass("cn.ymex.starter.model").forEach {
      println("----------------------:$it")
      val cna = PathScan.loadClass(it)
      cna?.run {
        if (this.isAnnotationPresent(Model::class.java)) {
          val instance = cna.newInstance()
          if (instance is AbstractModel<*, *>) {
            ModelRegister.models.add(instance)
          }
        }
      }
    }
    AppLauncher().dispatch(conf.toTypedArray())
  }
}


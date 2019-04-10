package cn.ymex.starter.main


object App {
  @JvmStatic
  fun main(args: Array<String>) {
    val conf = mutableListOf<String>()
    if (!args.contains("run")) {
      conf.add("run")
      conf.add("cn.ymex.starter.main.RootVerticle")
    }
    conf.addAll(args)
    AppLauncher().dispatch(conf.toTypedArray())
  }
}


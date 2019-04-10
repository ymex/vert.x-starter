package cn.ymex.starter.core

import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URLDecoder
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 *PathScan.scanClass("cn.ymex.starter.model").forEach {
 *     println("----------------------:$it")
 *     val cna = PathScan.loadClass(it)
 *     cna?.run {
 *       if (this.isAnnotationPresent(Model::class.java)) {
 *         val instance = cna.newInstance()
 *         if (instance is AbstractModel<*, *>) {
 *           ModelRegister.models.add(instance)
 *         }
 *       }
 *     }
 *   }
 */
object PathScan {


  fun loadClass(className: String): Class<*>? {
    try {
      return Class.forName(className)
    } catch (e: Throwable) {
    }
    return null
  }

  fun scanClass(pkg: String): Set<String> {
    val classes = LinkedHashSet<String>()

    val pkgDirName = pkg.replace('.', '/')
    try {
      val urls = getDefaultClassLoader()!!.getResources(pkgDirName)
      while (urls.hasMoreElements()) {
        val url = urls.nextElement()
        val protocol = url.protocol
        if ("file" == protocol) {// 如果是以文件的形式保存在服务器上
          val filePath = URLDecoder.decode(url.getFile(), "UTF-8")// 获取包的物理路径
          findClassesByFile(pkg, filePath, classes)
        } else if ("jar" == protocol) {// 如果是jar包文件
          val jar = (url.openConnection() as JarURLConnection).jarFile
          findClassesByJar(pkg, jar, classes)
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }

    return classes
  }

  private fun findClassesByFile(pkgName: String, pkgPath: String, classes: MutableSet<String>) {
    val dir = File(pkgPath)
    if (!dir.exists() || !dir.isDirectory) {
      return
    }


    // 过滤获取目录，or class文件
    val dirfiles = dir.listFiles { pathname -> pathname.isDirectory || pathname.name.endsWith("class") }

    if (dirfiles == null || dirfiles.size == 0) {
      return
    }

    var className: String
    for (f in dirfiles) {
      if (f.isDirectory) {
        findClassesByFile(
          pkgName + "." + f.name,
          pkgPath + "/" + f.name,
          classes
        )
        continue
      }

      className = f.name
      className = className.substring(0, className.length - 6)

      // 加载类

      classes.add("$pkgName.$className")

    }
  }

  private fun findClassesByJar(pkgName: String, jar: JarFile, classes: MutableSet<String>) {
    val pkgDir = pkgName.replace(".", "/")


    val entry = jar.entries()

    var jarEntry: JarEntry
    var name: String
    var className: String
    val claze: Class<*>
    while (entry.hasMoreElements()) {
      jarEntry = entry.nextElement()

      name = jarEntry.name
      if (name[0] == '/') {
        name = name.substring(1)
      }


      if (jarEntry.isDirectory || !name.startsWith(pkgDir) || !name.endsWith(".class")) {
        // 非指定包路径， 非class文件
        continue
      }


      // 去掉后面的".class", 将路径转为package格式
      className = name.substring(0, name.length - 6)
      classes.add(className.replace("/", "."))

    }
  }

  private fun getDefaultClassLoader(): ClassLoader? {
    var cl: ClassLoader? = null
    try {
      cl = Thread.currentThread().contextClassLoader
    } catch (ex: Throwable) {
      // Cannot access thread context ClassLoader - falling back...
    }

    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = PathScan::class.java.classLoader
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader()
        } catch (ex: Throwable) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }

      }
    }
    return cl
  }
}

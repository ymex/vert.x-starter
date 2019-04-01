package cn.ymex.starter.kits

import com.google.gson.Gson
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject

/**
 * buffer 转为实体bean
 */
inline fun <reified T> Buffer.toBean(): T {
  return Gson().fromJson<T>(toString(), T::class.java)
}

/**
 * string 转为实体bean
 */
inline fun <reified T> String.toBean(): T {
  return Gson().fromJson<T>(toString(), T::class.java)
}


/**
 * JsonObject 转为实体bean
 */
inline fun <reified T> JsonObject.toBean(): T {
  return Gson().fromJson<T>(toString(), T::class.java)
}


/**
 * 获取资源文件路径
 */
inline fun <reified T> getFileResourcePath(fileName:String): String {
  return T::class.java.classLoader.getResource(fileName).file
}

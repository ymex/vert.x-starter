package cn.ymex.starter.kits

import io.vertx.core.http.HttpServerResponse

/**
 * 输出内容格式
 */
fun HttpServerResponse.contentType(type: String): HttpServerResponse {
  this.putHeader("content-type", type)
  return this
}

/**
 * 输出内容格式 text
 */
fun HttpServerResponse.textContentType(): HttpServerResponse {
  this.putHeader("content-type", "text/plain")
  return this
}

/**
 * 输出内容格式 html
 */
fun HttpServerResponse.htmlContentType(): HttpServerResponse {
  this.putHeader("content-type", "text/html")
  return this
}

/**
 * 输出内容格式 json
 */
fun HttpServerResponse.jsonContentType(): HttpServerResponse {
  this.putHeader("content-type", "application/json")
  return this
}

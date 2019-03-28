package cn.ymex.starter

import io.vertx.core.http.HttpServerResponse


fun HttpServerResponse.contentType(type: String): HttpServerResponse {
  this.putHeader("content-type", type)
  return this
}

fun HttpServerResponse.textContentType(): HttpServerResponse {
  this.putHeader("content-type", "text/plain")
  return this
}

fun HttpServerResponse.htmlContentType(): HttpServerResponse {
  this.putHeader("content-type", "text/html")
  return this
}

fun HttpServerResponse.jsonContentType(): HttpServerResponse {
  this.putHeader("content-type", "application/json")
  return this
}

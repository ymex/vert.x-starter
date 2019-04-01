package cn.ymex.starter.main
import com.google.gson.annotations.SerializedName


data class DBConf(
    @SerializedName("database")
    val database: String,
    @SerializedName("host")
    val host: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("pool_size")
    val poolSize: Int,
    @SerializedName("port")
    val port: Int,
    @SerializedName("user")
    val user: String
)

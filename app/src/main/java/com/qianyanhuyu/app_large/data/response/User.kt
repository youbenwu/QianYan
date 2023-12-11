package com.qianyanhuyu.app_large.data.response
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("avatar")
    val avatar: String?

)

data class SecurityUser(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("session")
    val session: SecurityUserSession
)

data class SecurityUserSession(
    @SerializedName("token")
    val token: String
)
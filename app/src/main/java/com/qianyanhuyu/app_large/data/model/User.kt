package com.qianyanhuyu.app_large.data.model

import com.google.gson.annotations.SerializedName

/***
 * @Author : Cheng
 * @CreateDate : 2023/11/10 9:41
 * @Description : 用户信息
 */

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("avatar")
    val avatar: String?

)



data class SecurityUser(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("session")
    val session: SecurityUserSession? = null
)

data class SecurityUserSession(
    @SerializedName("token")
    val token: String?
)

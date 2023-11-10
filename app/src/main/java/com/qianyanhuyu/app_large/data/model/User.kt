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

data class Contact(
    @SerializedName("name")
    val name: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("address")
    val address: Address?
)

data class Address(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("province")
    val province: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("street")
    val street: String?,
    //street之后的详细地址
    @SerializedName("details")
    val details: String?,
    //完整地址
    @SerializedName("fullAddress")
    val fullAddress: String?,
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

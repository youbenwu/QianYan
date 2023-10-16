package com.qianyanhuyu.app_large.data.model

import com.google.gson.annotations.SerializedName

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/19 18:18
 * @Description : description
 */
data class ApiResult<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T,
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
package com.qianyanhuyu.app_large.data.response

import com.google.gson.annotations.SerializedName


data class Result<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?,
)

data class Page<T>(
    @SerializedName("content")
    val content:List<T>?,
    @SerializedName("totalElements")
    val totalElements:Int?,
    @SerializedName("totalPages")
    val totalPages:Int?,
    @SerializedName("pageable")
    val pageable:Pageable?
)

data class Pageable(
    @SerializedName("pageNumber")
    val pageNumber:Int?,
    @SerializedName("pageSize")
    val pageSize:Int?
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

data class 	Media(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("type")
    val type: Int?,
)

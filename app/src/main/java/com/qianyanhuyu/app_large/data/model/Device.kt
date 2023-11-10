package com.qianyanhuyu.app_large.data.model

/***
 * @Author : Cheng
 * @CreateDate : 2023/11/10 9:35
 * @Description : description
 */

import com.google.gson.annotations.SerializedName

data class RegisterDevice(
    @SerializedName("userId")
    var userId: Int?,
    @SerializedName("hotelName")
    var hotelName: String?,
    @SerializedName("roomNo")
    var roomNo: String?,
    @SerializedName("deviceNo")
    var deviceNo: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?,
    @SerializedName("address")
    var address: Address?
)

data class Device(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("hotelId")
    val hotelId: Int,
    @SerializedName("nickname")
    val roomNo: String,
    @SerializedName("deviceNo")
    val deviceNo: String,
    @SerializedName("updateTime")
    val updateTime: String
)
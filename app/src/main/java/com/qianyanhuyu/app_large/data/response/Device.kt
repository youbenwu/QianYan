package com.qianyanhuyu.app_large.data.response

import com.google.gson.annotations.SerializedName


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

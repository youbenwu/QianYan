package com.qianyanhuyu.app_large.data.hotel.model

import com.google.gson.annotations.SerializedName
import com.qianyanhuyu.app_large.data.model.Address
import retrofit2.http.Field

data class RegisterDeviceDTO(
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
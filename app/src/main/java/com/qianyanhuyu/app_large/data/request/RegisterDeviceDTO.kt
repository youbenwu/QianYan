package com.qianyanhuyu.app_large.data.request

import com.google.gson.annotations.SerializedName
import com.qianyanhuyu.app_large.data.response.Address

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


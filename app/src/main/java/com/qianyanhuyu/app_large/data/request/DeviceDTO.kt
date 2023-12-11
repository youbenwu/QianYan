package com.qianyanhuyu.app_large.data.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class DeviceDTO(
    @SerializedName("id")
    @Field("id") var id: Int?,
    @SerializedName("actUserId")
    @Field("actUserId") var actUserId: Int,
    @SerializedName("hotelId")
    @Field("hotelId") var hotelId: Int,
    @SerializedName("roomNo")
    @Field("roomNo") var roomNo: String,
    @SerializedName("deviceNo")
    @Field("deviceNo") var deviceNo: String,
    @SerializedName("name")
    @Field("name") var name: String?
)




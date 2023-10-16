package com.qianyanhuyu.app_large.data.hotel.model
import com.google.gson.annotations.SerializedName
import com.qianyanhuyu.app_large.data.model.Contact


data class Hotel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("shopId")
    val shopId: Int?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("statusRemark")
    val statusRemark: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("intro")
    val intro: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("contact")
    val contact: Contact?,
    @SerializedName("createTime")
    val createTime: String?,
    @SerializedName("updateTime")
    val updateTime: String?
)


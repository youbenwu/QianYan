package com.qianyanhuyu.app_large.data.user.model
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("person_num")
    val personNum: String,
    @SerializedName("person_phone")
    val personPhone: String,
    @SerializedName("code")
    val code: String

)
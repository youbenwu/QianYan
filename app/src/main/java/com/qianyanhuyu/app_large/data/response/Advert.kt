package com.qianyanhuyu.app_large.data.response

import com.google.gson.annotations.SerializedName

data class Advert(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("channelId")
    var channelId: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("subtitle")
    var subtitle: String?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("url")
    var url: String?,
)


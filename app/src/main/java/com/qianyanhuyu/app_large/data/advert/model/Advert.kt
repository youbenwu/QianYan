package com.qianyanhuyu.app_large.data.advert.model

import com.google.gson.annotations.SerializedName
import com.qianyanhuyu.app_large.data.model.Address

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


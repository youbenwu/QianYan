package com.qianyanhuyu.app_large.data.request

import com.google.gson.annotations.SerializedName
import com.qianyanhuyu.app_large.data.response.Address

data class GetProductListDTO(
    @SerializedName("page")
    var page: Int?,
    @SerializedName("size")
    var size: Int?,
    @SerializedName("type")
    var type: Int?,
    @SerializedName("shopId")
    var shopId: Int?,
    @SerializedName("spcId")
    var spcId: Int?,

)


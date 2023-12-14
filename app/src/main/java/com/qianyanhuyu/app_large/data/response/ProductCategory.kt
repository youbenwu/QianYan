package com.qianyanhuyu.app_large.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class ProductCategory(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("children")
    val children:List<ProductCategory>?

)
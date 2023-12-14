package com.qianyanhuyu.app_large.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Product(
    @SerializedName("id")
    val id: Int?=null,
    @SerializedName("title")
    val title: String?=null,
    @SerializedName("subtitle")
    val subTitle: String?=null,
    @SerializedName("image")
    val image: String?=null,
    @SerializedName("images")
    val images:List<Media>?=null,
    val medias:List<Media>?=null,
    @SerializedName("price")
    val price: Double?=null,
    //销售量
    @SerializedName("sales")
    val sales:Int?=null,
    val properties:List<ProductProperty>?=null,
    val skus:List<ProductSku>?=null,


)
@Keep
data class ProductProperty(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value:String?,
    val items:List<ProductPropertyItem>?
)

data class ProductPropertyItem(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("value")
    val value:String?,
)

@Keep
data class ProductSku(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("stock")
    val stock: Int?,
    @SerializedName("images")
    val images:List<Media>?,
)


package com.qianyanhuyu.app_large.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/30 11:31
 * @Description : 产品模型
 */

/**
 * 产品对象
 * @param image 图片地址
 * @param price 商品价格
 * @param title 商品标题
 * @param subTitle 商品副标题
 */
@Keep
data class Product(
    @SerializedName("image")
    val image: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subTitle: String?,
)
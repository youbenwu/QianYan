package com.qianyanhuyu.app_large.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/30 11:10
 * @Description : 产品
 */

/**
 * 产品Request
 * @param shopId 店铺Id, 获取设备信息接口有返回
 * @param type 商品类型, 干洗服务使用 30
 */
@Keep
data class ProductListRequest(
    @SerializedName("shopId")
    val shopId: Int,
    @SerializedName("type")
    val type: Int
)
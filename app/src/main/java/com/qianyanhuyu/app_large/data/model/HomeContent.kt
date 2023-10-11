package com.qianyanhuyu.app_large.data.model

import com.google.gson.annotations.SerializedName

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/21 13:22
 * @Description : 主页数据类
 */
data class HomeContent (
    val id: String
)

data class MediaData(
    @SerializedName("banner_src")
    val bannerSrc: String
)
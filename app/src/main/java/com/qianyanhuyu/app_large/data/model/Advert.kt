package com.qianyanhuyu.app_large.data.model

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/26 9:42
 * @Description : 广告
 */

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Advert(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("channelId")
    var channelId: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("subtitle")
    var subTitle: String?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("video")
    var video: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("qrCode")
    val qrCode: String?,
    @SerializedName("advertType")
    val advertType: AdvertType?
)

/**
 * 广告显示类型
 */
enum class AdvertType(
    val value: String
) {
    /**
     * 普通图片视频广告
     */
    PPV("PPV"),

    /**
     * 带广告链接
     */
    PPC("PPC"),

    /**
     * 可扫描二维码
     */
    PPA("PPA")
}

/**
 * 广告内容类型
 */
enum class AdvertContentType(
    val value: String
) {
    /**
     * 图片
     */
    Image("image"),

    /**
     * 视频
     */
    Video("video"),
}

/**
 * 用来请求广告列表的类型参数
 * 对应不同页面
 */
enum class AdvertTypeRequest(
    val value: String
) {
    /**
     * 默认首页广告
     */
    PadHomeDef("pad-home-def"),

    /**
     * 首页广告
     */
    PadHome("pad-home"),

    /**
     * 副页广告
     */
    PadSub("pad-sub"),

    /**
     * 客服服务页广告
     */
    PadServices("pad-services"),

    /**
     * 迁眼互娱影视页
     */
    PadMovies("pad-movies"),

    /**
     * 迁眼送页
     */
    PadGames("pad-games"),

    /**
     * 智慧旅游页
     */
    PadTours("pad-tours")
}
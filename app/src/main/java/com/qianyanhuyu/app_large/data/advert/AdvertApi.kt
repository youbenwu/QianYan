package com.qianyanhuyu.app_large.data.advert

import com.qianyanhuyu.app_large.data.advert.model.Advert
import com.qianyanhuyu.app_large.data.hotel.model.Device
import com.qianyanhuyu.app_large.data.hotel.model.RegisterDeviceDTO
import com.qianyanhuyu.app_large.data.model.ApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AdvertApi {

    /**
     * /api/portal/advert/list
     * 获取广告信息列表 channelCode–传下面几个
     * 默认首页广告:pad-home-def
     * 首页广告:pad-home
     * 客服服务页广告:pad-services
     * 迁眼互娱影视页:pad-movies
     * 迁眼送页:pad-games
     * 智慧旅游页:pad-tours
     * */
    @FormUrlEncoded
    @POST("/api/portal/advert/list")
    suspend fun getAdvertList(
        @Field("channelCode") channelCode: String,
        @Field("size") size: Int,
    ): Response<ApiResult<List<Advert>>>



}
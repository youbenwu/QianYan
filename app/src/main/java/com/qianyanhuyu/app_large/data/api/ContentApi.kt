package com.qianyanhuyu.app_large.data.api

import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.model.Device
import com.qianyanhuyu.app_large.data.model.Product
import com.qianyanhuyu.app_large.data.model.ProductDetails
import com.qianyanhuyu.app_large.data.model.RegisterDevice
import com.qianyanhuyu.app_large.data.request.ProductListRequest
import com.qianyanhuyu.app_large.data.response.BaseCommonPaging
import com.qianyanhuyu.app_large.data.response.BasePaging
import com.qianyanhuyu.app_large.data.response.BaseResponse
import com.qianyanhuyu.app_large.model.GroupChatItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 通用内容Api
 */
interface ContentApi {
    @GET("getChats")
    suspend fun getGroupChats(
        @Query("page") page: Int = 1,
        @Query("page_size")pageSize: Int = 10
    ): Response<BaseResponse<BasePaging<GroupChatItem>>>


    /**
     * 商品列表
     */
    @POST("api/mall/product/page")
    suspend fun getProduct(
        @Body request: ProductListRequest
    ) : Response<BaseResponse<BaseCommonPaging<Product>>>

    @POST("api/mall/product/get")
    suspend fun getProductDetails(
        @Query("id") id: Int,
    ) : Response<BaseResponse<ProductDetails>>


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
    @POST("api/portal/advert/list")
    suspend fun getAdvertList(
        @Field("channelCode") channelCode: String,
        @Field("size") size: Int,
    ): Response<BaseResponse<List<Advert>>>

    /**
     * 获取广告内容分页接口
     */
    @FormUrlEncoded
    @POST("api/portal/advert/page")
    suspend fun getAdvertPage(
        @Field("page") page: Int,
        @Field("size") size: Int,
    ): Response<BaseResponse<BaseCommonPaging<Advert>>>

    /**
     * /api/hotel/device/getByDeviceNo
     * 获取设备
     * 激活后可获取到设备信息
     * */
    @POST("/api/hotel/device/register")
    suspend fun registerDevice(
        @Body data: RegisterDevice
    ): Response<BaseResponse<Device>>


    /**
     * /api/hotel/device/getByDeviceNo
     * 获取设备
     * 激活后可获取到设备信息
     * */
    @FormUrlEncoded
    @GET("/api/hotel/device/getByDeviceNo")
    suspend fun getDevice(
        @Field("deviceNo") deviceNo: String
    ): Response<BaseResponse<Device>>




}
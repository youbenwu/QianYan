package com.qianyanhuyu.app_large.data.hotel

import com.qianyanhuyu.app_large.data.hotel.model.Device
import com.qianyanhuyu.app_large.data.hotel.model.RegisterDeviceDTO
import com.qianyanhuyu.app_large.data.model.ApiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface HotelApi {

    /**
     * /api/hotel/device/getByDeviceNo
     *
     * 激活设备信息
     * */
    @POST("/api/hotel/device/register")
    suspend fun registerDevice(
        @Body data:RegisterDeviceDTO
    ): Response<ApiResult<Device>>


    /**
     * /api/hotel/device/getByDeviceNo
     * 获取设备
     * 激活后可获取到设备信息
     * */
    @FormUrlEncoded
    @POST("/api/hotel/device/getByDeviceNo")
    suspend fun getDevice(
        @Field("deviceNo") deviceNo: String
    ): Response<ApiResult<Device>>



}
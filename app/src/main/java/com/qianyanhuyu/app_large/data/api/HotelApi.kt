package com.qianyanhuyu.app_large.data.api

import com.qianyanhuyu.app_large.data.request.DeviceDTO
import com.qianyanhuyu.app_large.data.request.RegisterDeviceDTO
import com.qianyanhuyu.app_large.data.response.Device
import com.qianyanhuyu.app_large.data.response.Result
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HotelApi {

    /**
     * 激活设备信息
     * */
    @POST("/api/hotel/device/register")
    suspend fun registerDevice(
        @Body data: RegisterDeviceDTO
    ): Response<Result<Device>>

    /**
     * 激活设备信息
     * */
    @POST("/api/hotel/device/save")
    suspend fun saveDevice(
        @FieldMap data: DeviceDTO
    ): Response<Result<Device>>

    /**
     * /api/hotel/device/getByDeviceNo
     * 获取设备
     * 激活后可获取到设备信息
     * */
    @FormUrlEncoded
    @POST("/api/hotel/device/getByDeviceNo")
    suspend fun getDevice(
        @Field("deviceNo") deviceNo: String
    ): Response<Result<Device>>



}
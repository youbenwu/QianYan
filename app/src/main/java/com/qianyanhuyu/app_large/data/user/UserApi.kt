package com.qianyanhuyu.app_large.data.user

import com.qianyanhuyu.app_large.data.model.ApiResult
import com.qianyanhuyu.app_large.data.user.model.SecurityUser
import com.qianyanhuyu.app_large.data.user.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {



    /**
     * 获取用户信息
     * */
    @FormUrlEncoded
    @GET("/api/user/get")
    suspend fun getUser(
        @Field("id") id: String
    ): Response<ApiResult<User>>

    /**
     * 账号密码登陆
     * */
    @FormUrlEncoded
    @POST("/api/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ApiResult<SecurityUser>>


    /**
     * 手机验证码登陆
     * */
    @FormUrlEncoded
    @POST("/api/user/login/phone")
    suspend fun loginByPhone(
        @Field("phone") phone: String,
        @Field("verifyCode") verifyCode: String
    ): Response<ApiResult<SecurityUser>>

    /**
     *
     * 获取手机验证码
     * */
    @FormUrlEncoded
    @POST("/api/user/smsCode")
    suspend fun smsCode(
        @Field("phone") phone: String,
        @Field("type") type: String
    ): Response<ApiResult<User>>



}
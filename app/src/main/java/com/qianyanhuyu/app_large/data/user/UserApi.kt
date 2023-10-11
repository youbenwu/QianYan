package com.qianyanhuyu.app_large.data.user

import com.qianyanhuyu.app_large.data.user.model.response.BaseResponse
import com.qianyanhuyu.app_large.data.user.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    /**
     * 获取用户信息
     * */
    @GET("getUser/")
    suspend fun getUser(
        @Query("access_token") accessToken: String
    ): Response<BaseResponse<User>>
}
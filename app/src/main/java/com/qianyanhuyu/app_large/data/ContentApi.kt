package com.qianyanhuyu.app_large.data

import com.qianyanhuyu.app_large.data.response.BasePaging
import com.qianyanhuyu.app_large.data.response.BaseResponse
import com.qianyanhuyu.app_large.model.GroupChatItem
import retrofit2.Response
import retrofit2.http.GET
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
}
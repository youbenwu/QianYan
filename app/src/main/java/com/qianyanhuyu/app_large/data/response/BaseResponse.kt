package com.qianyanhuyu.app_large.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/19 18:18
 * @Description : description
 */
@Keep
data class BaseResponse<T : Any>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T,
) : Serializable

@Keep
data class BasePaging<T : Any>(
    @SerializedName("page_count")
    val pageCount: Int = 1,
    @SerializedName("current_page")
    val currentPage: Int = 1,
    @SerializedName("hasMore")
    val hasMore: Boolean = false,
    @SerializedName("comments")
    val comments: List<T> = emptyList(),
)
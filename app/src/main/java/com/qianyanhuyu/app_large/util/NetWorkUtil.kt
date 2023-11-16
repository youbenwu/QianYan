package com.qianyanhuyu.app_large.util

import com.qianyanhuyu.app_large.data.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.Response

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/26 11:20
 * @Description : 网络请求Flow流
 *
 * TODO errorBlock 是错误处理的回调,看使用需求增加相应处理,现在暂无处理
 */
suspend fun <T : Any> requestFlowResponse(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    requestCall: suspend () -> Response<BaseResponse<T>>?,
    showLoading: ((Boolean) -> Unit)? = null
): T? {
    var data: T? = null

    flow {
        val response = requestCall()

        // 错误处理
        /*if (response.errorBody())*/

        emit(response)
    }.flowOn(Dispatchers.IO)
        .onStart {
            // 开始请求
            showLoading?.invoke(true)
        }
        .catch { e ->
            e.printStackTrace()

        }
        .onCompletion {
            showLoading?.invoke(false)
        }
        .collect {
            data = it?.body()?.data
        }

    return data
}
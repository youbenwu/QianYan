package com.qianyanhuyu.app_large.data

import com.qianyanhuyu.app_large.data.response.Result
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
suspend fun <T : Any> FlowResult(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    dataBlock: ((T?) -> Unit)? = null,
    requestCall: suspend () -> Response<Result<T>>?,
    loading: ((Boolean) -> Unit)? = null
): T? {
    var data: T? = null

    flow {
        val response = requestCall()

        // 错误处理
        if (response?.isSuccessful==false){
            errorBlock?.invoke(response?.code(),response?.message())
        }else if(response?.body()?.status!=0){
            errorBlock?.invoke(response?.body()?.status,response?.body()?.message)
        }else{
            dataBlock?.invoke(response?.body()?.data)
        }
        emit(response)
    }.flowOn(Dispatchers.IO)
        .onStart {
            // 开始请求
            loading?.invoke(true)
        }
        .catch { e ->
            e.printStackTrace()

        }
        .onCompletion {
            loading?.invoke(false)
        }
        .collect {
            data = it?.body()?.data
        }

    return data
}
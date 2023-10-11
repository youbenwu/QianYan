package com.qianyanhuyu.app_large.model

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/19 16:16
 * @Description : Loading 状态类
 */
sealed class LoadingState<out R> {
    object Loading : LoadingState<Nothing>()
    data class Failure(val error : Throwable) : LoadingState<Nothing>()
    data class Success<T>(val data : T) : LoadingState<T>()

    val isLoading
        get() = this is Loading
    val isSuccess
        get() = this is Success<*>
}
package com.qianyanhuyu.app_large.data.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.response.BasePaging

/***
 * @Author : DT
 * @CreateDate : 2023/10/18 15:05
 * @Description : 定义数据加载方式
 */
class CommonPageDataSource<T : Any>(
    private val contentApi: ContentApi
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        // 根据preKey和nextKey中找到离anchorPosition最近页面的键值
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        // 定义键值
        val currentKey = params.key ?: 1
        return try {
            val response = contentApi.getGroupChats(
                page = currentKey
            )

            if(response.isSuccessful && response.body() != null) {
                val pageData = response.body()?.data as? BasePaging<T>
                val dataList = pageData?.comments ?: emptyList()
                LoadResult.Page(
                    data = dataList,
                    prevKey = if (currentKey == 1) null else currentKey.minus(1),
                    nextKey = if (currentKey == (pageData?.pageCount ?: currentKey)) null else currentKey.plus(1)
                )
            } else {
                LoadResult.Error(PagingException(response.code().toString(), response.message() ?: "未知错误"))
            }

        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}

class PagingException(val errorCode: String, val errorMessage: String) : Exception("PagingException")
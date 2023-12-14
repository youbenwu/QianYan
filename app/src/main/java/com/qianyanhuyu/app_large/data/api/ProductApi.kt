package com.qianyanhuyu.app_large.data.api

import com.qianyanhuyu.app_large.data.request.GetProductListDTO
import com.qianyanhuyu.app_large.data.response.Page
import com.qianyanhuyu.app_large.data.response.Product
import com.qianyanhuyu.app_large.data.response.Result
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProductApi {

    /**
     * 获取商品列表
     * */
    @POST("/api/mall/product/page")
    suspend fun getProductPage(
        @Body data: GetProductListDTO
    ): Response<Result<Page<Product>>>


}
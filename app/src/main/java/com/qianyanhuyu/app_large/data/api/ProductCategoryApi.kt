package com.qianyanhuyu.app_large.data.api

import com.qianyanhuyu.app_large.data.request.GetProductListDTO
import com.qianyanhuyu.app_large.data.response.Page
import com.qianyanhuyu.app_large.data.response.Product
import com.qianyanhuyu.app_large.data.response.ProductCategory
import com.qianyanhuyu.app_large.data.response.Result
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProductCategoryApi {

    /**
     * 获取商品列表
     * */
    @FormUrlEncoded
    @POST("/api/mall/product/category/category/list")
    suspend fun getProductCategoryList(
    ): Response<Result<List<ProductCategory>>>


}
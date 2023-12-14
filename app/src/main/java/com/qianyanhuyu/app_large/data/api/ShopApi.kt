package com.qianyanhuyu.app_large.data.api


import com.qianyanhuyu.app_large.data.response.ProductCategory
import com.qianyanhuyu.app_large.data.response.Result
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ShopApi {

    /**
     * 获取店铺分类列表
     * */
    @FormUrlEncoded
    @POST("/api/mall/shop/product/category/list")
    suspend fun getShopProductCategoryList(
        @Field("shopId") shopId: Int?,
        @Field("productType") productType: Int?
    ): Response<Result<List<ProductCategory>>>


}
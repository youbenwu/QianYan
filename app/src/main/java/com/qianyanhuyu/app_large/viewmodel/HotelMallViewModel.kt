package com.qianyanhuyu.app_large.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.api.ContentApi
import com.qianyanhuyu.app_large.data.api.ProductApi
import com.qianyanhuyu.app_large.data.api.ShopApi
import com.qianyanhuyu.app_large.data.request.GetProductListDTO
import com.qianyanhuyu.app_large.data.response.Product
import com.qianyanhuyu.app_large.data.request.ProductListRequest
import com.qianyanhuyu.app_large.data.response.ProductCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class HotelMallViewModel @Inject constructor(
    private val shopApi: ShopApi,
    private val productApi: ProductApi,
) : ViewModel() {

    var viewStates by mutableStateOf(HotelMallViewState())
        private set

    private val _viewEvents = Channel<HotelMallViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(HotelMallViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: HotelMallViewAction) {
        when (action) {
            is HotelMallViewAction.InitPageData -> initPageData()
            is HotelMallViewAction.SelectedCategory -> selectedCategory(action.categoryId)
            else -> {

            }
        }
    }



    @OptIn(FlowPreview::class)
    private fun initPageData() {
        viewModelScope.launch(exception) {
            viewStates = viewStates.copy(
                isLoading = true
            )
            val response=shopApi.getShopProductCategoryList(
                shopId = viewStates.shopId,
                productType =viewStates.productType
            )

            viewStates = viewStates.copy(
                isLoading = false
            )

            if(response.isSuccessful){
                val result=response.body();
                if(result?.status==0){
                    viewStates = viewStates.copy(
                        categorys= result.data!!,
                    )
                    if(viewStates.categorys.size>0){
                        selectedCategory(viewStates.categorys[0].id!!)
                    }
                }else{
                    _viewEvents.send(HotelMallViewEvent.ShowMessage(result?.message+""))
                }
            }else{
                _viewEvents.send(HotelMallViewEvent.ShowMessage("网络出错了"))
            }
        }
    }

    private fun selectedCategory(categoryId:Int){
        viewModelScope.launch(exception) {
            viewStates = viewStates.copy(
                isLoading = true
            )
            val response=productApi.getProductPage(
                data = GetProductListDTO(
                    type=viewStates.productType,
                    shopId = viewStates.shopId,
                    spcId = categoryId,
                    size = 9,
                    page = 0,
                )
            )
            viewStates = viewStates.copy(
                isLoading = false
            )

            if(response.isSuccessful){
                val result=response.body();
                if(result?.status==0){
                    viewStates = viewStates.copy(
                        products= result.data?.content!!,
                    )
                }else{
                    _viewEvents.send(HotelMallViewEvent.ShowMessage(result?.message+""))
                }
            }else{
                _viewEvents.send(HotelMallViewEvent.ShowMessage("网络出错了"))
            }
        }
    }


}

data class HotelMallViewState(
    val shopId: Int = 1104,
    var productType: Int = 31,
    val categorys: List<ProductCategory> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
)

sealed class HotelMallViewAction {
    object InitPageData : HotelMallViewAction()

    data class SelectedCategory(
        val categoryId:Int
    ) : HotelMallViewAction()

}

sealed class HotelMallViewEvent {
    data class NavTo(val route: String) : HotelMallViewEvent()
    data class ShowMessage(val message: String) : HotelMallViewEvent()

}


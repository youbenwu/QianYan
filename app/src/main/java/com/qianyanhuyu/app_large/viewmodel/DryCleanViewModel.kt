package com.qianyanhuyu.app_large.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.Product
import com.qianyanhuyu.app_large.data.model.ProductAttributes
import com.qianyanhuyu.app_large.data.request.ProductListRequest
import com.qianyanhuyu.app_large.util.requestFlowResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/25 11:27
 * @Description : 干洗服务
 */
@HiltViewModel
class DryCleanViewModel @Inject constructor(
    private val contentApi: ContentApi,
) : ViewModel() {

    var viewStates by mutableStateOf(DryCleanViewState())
        private set

    private val _viewEvents = Channel<DryCleanViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(DryCleanViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: DryCleanViewAction) {
        when (action) {
            is DryCleanViewAction.InitPageData -> initPageData()
            is DryCleanViewAction.CountChange -> countChange(action.calculate, action.data)
            is DryCleanViewAction.GetProductDetail -> getProductDetails(action.id)
            is DryCleanViewAction.CheckItem -> checkItem(
                id = action.id,
                isCheck = action.isCheck
            )
            else -> {

            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun initPageData() {
        viewModelScope.launch(exception) {
            // 先获取产品,再获取产品详情
            flow {
                val response = contentApi.getProduct(
                    request = ProductListRequest(
                        // TODO 使用设备信息接口数据中的 shopId, 现在暂时未接入
                        shopId = 749,
                        type = 30
                    )
                )
                emit(response)
            }.flatMapConcat { productResponse ->
                flow {
                    val productData = productResponse.body()?.data?.content ?: emptyList()
                    val typeData = productData.mapIndexed { index, advert ->
                        DryCleanType(
                            name = advert.title ?: "",
                            type = index,
                        )
                    }
                    Log.d("DryClearViewModel: ", "data: ${productData}")

                    val firstData = productData.getOrNull(0)

                    viewStates = viewStates.copy(
                        typeData = typeData,
                        data = productData,
                        totalPrice = firstData?.price?.let {
                            BigDecimal(it)
                        } ?: BigDecimal.ZERO
                    )

                    // kotlinx.coroutines.delay(5000)
                    val response = contentApi.getProductDetails(
                        id = firstData?.id ?: -1
                    )
                    emit(response)
                }
            }.flowOn(Dispatchers.IO)
                .onStart {
                    // 开始请求
                    viewStates = viewStates.copy(
                        isLoading = true
                    )
                }
                .catch { e ->
                    e.printStackTrace()

                }
                .onCompletion {
                    viewStates = viewStates.copy(
                        isLoading = false
                    )
                }
                .collect {
                    val data = it.body()?.data

                    val items = data?.attributes?.firstOrNull()?.attributes

                    val checkItem = mutableListOf<ProductAttributes>()
                    val checkItems = mutableListOf<List<ProductAttributes>>()

                    items?.forEach { attributes ->
                        checkItem.add(attributes)
                        if(checkItem.count() == 2) {
                            checkItems.add(checkItem)
                        }
                    }

                    viewStates = viewStates.copy(
                        data = viewStates.data.map { productData ->
                            // 初次加载只获取第一个Id
                            if(productData.id == (viewStates.data.firstOrNull()?.id ?: -1)) {
                                productData.copy(
                                    details = data
                                )
                            } else {
                                productData
                            }
                        },
                        checkItems = checkItems
                    )
                }
        }
    }

    /**
     * 获取产品详情
     * @param productId 产品Id
     */
    private fun getProductDetails(
        productId: Int
    ) {
        if(viewStates.data.firstOrNull { it.id == productId }?.details != null) {
            return
        }

        viewModelScope.launch {
            requestFlowResponse(
                requestCall = {
                    contentApi.getProductDetails(
                        id = productId
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this
                Log.d("DryCleanData: ", data.toString())

                viewStates = viewStates.copy(
                    data = viewStates.data.map { productData ->
                        if(productData.id == productId) {
                            productData.copy(
                                details = data
                            )
                        } else {
                            productData
                        }
                    },
                )
            }
        }
    }

    /**
     * 选中附加项
     */
    private fun checkItem(
        id: Int,
        isCheck: Boolean,
    ) {
        var itemPrice = BigDecimal.ZERO
        Log.d("ISSSSSCHECK: ", "$isCheck")

        viewStates = viewStates.copy(
            checkItems = viewStates.checkItems.map {
                it?.map { attributes ->
                    if(attributes.id == id) {
                        itemPrice = attributes.value?.let { a ->
                            BigDecimal(a)
                        } ?: itemPrice
                        attributes.copy(
                            isCheckCheckBox = isCheck
                        )
                    } else {
                        attributes
                    }
                }
            },
            totalPrice = if(isCheck) {
                viewStates.totalPrice.add(itemPrice)
            } else {
                viewStates.totalPrice.minus(itemPrice)
            }
        )
    }

    private fun countChange(
        calculate: AppConfig.Calculate,
        data: Product,
    ) {
        var totalPrice = viewStates.totalPrice

        val newData = viewStates.data.map {
            if(it.id == data.id) {
                val oldCount = it.countProduct ?: 1
                val newCount = when(calculate) {
                    AppConfig.Calculate.MINUS -> {
                        if(oldCount > 1) {
                            totalPrice = totalPrice.minus(BigDecimal((it.price ?: 0.0).toDouble()))
                            oldCount - 1
                        } else {
                            oldCount
                        }
                    }
                    AppConfig.Calculate.ADD -> {
                        totalPrice = totalPrice.add(BigDecimal((it.price ?: 0.0).toDouble()))
                        oldCount + 1
                    }
                    else -> {
                        oldCount
                    }
                }

                it.copy(
                    countProduct = newCount
                )
            } else {
                it
            }
        }

        viewStates = viewStates.copy(
            data = newData,
            totalPrice = totalPrice
        )
    }
}

data class DryCleanViewState(
    val data: List<Product> = emptyList(),
    val typeData: List<DryCleanType> = emptyList(),
    val checkItems: List<List<ProductAttributes>?> = emptyList(),
    val totalPrice: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = true,
)

/**
 * 干洗类型选择对象
 * @param name 分类名称
 * @param type 分类Id
 * @param title 标题
 */
data class DryCleanType(
    val name: String,
    val type: Int,
)

sealed class DryCleanViewAction {
    object InitPageData : DryCleanViewAction()

    data class CountChange(
        val calculate: AppConfig.Calculate = AppConfig.Calculate.EMPTY,
        val data: Product,
    ): DryCleanViewAction()

    data class GetProductDetail(
        val id: Int
    ): DryCleanViewAction()


    data class CheckItem(
        val id: Int,
        val isCheck: Boolean
    ): DryCleanViewAction()
}

sealed class DryCleanViewEvent {
    data class NavTo(val route: String) : DryCleanViewEvent()
    data class ShowMessage(val message: String) : DryCleanViewEvent()
}
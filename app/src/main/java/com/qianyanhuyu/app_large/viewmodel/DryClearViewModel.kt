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
import com.qianyanhuyu.app_large.data.request.ProductListRequest
import com.qianyanhuyu.app_large.util.requestFlowResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            requestFlowResponse(
                requestCall = {
                    contentApi.getProduct(
                        request = ProductListRequest(
                            // TODO 使用设备信息接口数据中的 shopId, 现在暂时未接入
                            shopId = 749,
                            type = 30
                        )
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this?.content ?: emptyList()
                Log.d("DryCleanData: ", data.toString())
                // 根据产品列表数据封装选择分类类型数据
                val typeData = data.mapIndexed { index, advert ->
                    DryCleanType(
                        name = advert.title ?: "",
                        type = index,
                        title = advert.subTitle ?: "",
                    )
                }

                viewStates = viewStates.copy(
                    typeData = typeData,
                    data = data,
                )
            }
        }
    }

    private fun countChange(
        calculate: AppConfig.Calculate,
        data: Product,
    ) {
        viewStates = viewStates.copy(
            data = viewStates.data.map {
                if(it.id == data.id) {
                    val oldCount = it.count ?: 1
                    it.copy(
                        count = when(calculate) {
                            AppConfig.Calculate.MINUS -> {
                                oldCount - 1
                            }
                            AppConfig.Calculate.ADD -> {
                                oldCount + 1
                            }
                            else -> {
                                oldCount
                            }
                        }
                    )
                } else {
                    it
                }
            }
        )
    }
}

data class DryCleanViewState(
    val data: List<Product> = emptyList(),
    val typeData: List<DryCleanType> = emptyList(),
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
    val title: String = "",
)

sealed class DryCleanViewAction {
    object InitPageData : DryCleanViewAction()

    data class CountChange(
        val calculate: AppConfig.Calculate = AppConfig.Calculate.EMPTY,
        val data: Product,
    ): DryCleanViewAction()
}

sealed class DryCleanViewEvent {
    data class NavTo(val route: String) : DryCleanViewEvent()
    data class ShowMessage(val message: String) : DryCleanViewEvent()
}
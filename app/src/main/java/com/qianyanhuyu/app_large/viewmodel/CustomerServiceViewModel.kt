package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.model.AdvertTypeRequest
import com.qianyanhuyu.app_large.util.requestFlowResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : 客房服务
 */
@HiltViewModel
class CustomerServiceViewModel @Inject constructor(
    private val contentApi: ContentApi
) : ViewModel() {

    var viewStates by mutableStateOf(CustomerServiceViewState())
        private set

    private val _viewEvents = Channel<CustomerServiceViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(CustomerServiceViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: CustomerServiceViewAction) {
        when (action) {
            is CustomerServiceViewAction.InitPageData -> initPageData()
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertList(
                        AdvertTypeRequest.PadServices.value,
                        4
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this ?: emptyList()
                viewStates = viewStates.copy(
                    data = data,
                )
            }
        }
    }


}

data class CustomerServiceViewState(
    val data: List<Advert> = emptyList(),
    val isLoading: Boolean = true,
)

sealed class CustomerServiceViewAction {
    object InitPageData : CustomerServiceViewAction()
}

sealed class CustomerServiceViewEvent {
    data class NavTo(val route: String) : CustomerServiceViewEvent()
    data class ShowMessage(val message: String) : CustomerServiceViewEvent()
}
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
 * @CreateDate : 2023/9/23 11:27
 * @Description : 迁眼送
 */
@HiltViewModel
class QianYanGiveViewModel @Inject constructor(
    private val contentApi: ContentApi
) : ViewModel() {

    var viewStates by mutableStateOf(QianYanGiveViewState())
        private set

    private val _viewEvents = Channel<QianYanGiveViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(QianYanGiveViewEvent.ShowMessage("错误："+throwable.message))
        }
    }


    fun dispatch(action: QianYanGiveViewAction) {
        when (action) {
            is QianYanGiveViewAction.InitPageData -> initPageData()
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertList(
                        AdvertTypeRequest.PadGames.value,
                        3
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

data class QianYanGiveViewState(
    val isLoading: Boolean = true,
    val data: List<Advert> = emptyList()
)

sealed class QianYanGiveViewAction {
    object InitPageData : QianYanGiveViewAction()
}

sealed class QianYanGiveViewEvent {
    data class NavTo(val route: String) : QianYanGiveViewEvent()
    data class ShowMessage(val message: String) : QianYanGiveViewEvent()
}
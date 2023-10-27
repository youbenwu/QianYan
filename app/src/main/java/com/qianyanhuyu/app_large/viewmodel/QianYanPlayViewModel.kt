package com.qianyanhuyu.app_large.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.model.AdvertTypeRequest
import com.qianyanhuyu.app_large.data.model.MediaData
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
 * @Description : 迁眼互娱
 */
@HiltViewModel
class QianYanPlayViewModel @Inject constructor(
    private val contentApi: ContentApi
) : ViewModel() {

    var viewStates by mutableStateOf(QianYanPlayViewState())
        private set

    private val _viewEvents = Channel<QianYanPlayViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(QianYanPlayViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: QianYanPlayViewAction) {
        when (action) {
            is QianYanPlayViewAction.InitPageData -> initPageData()
            is QianYanPlayViewAction.CheckIsVip -> checkIsVip(action.isShowTripsDialog)
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertList(
                        channelCode = AdvertTypeRequest.PadMovies.value,
                        size = 5
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                viewStates = viewStates.copy(
                    data = this ?: emptyList()
                )
            }
        }
    }

    private fun checkIsVip(isShow: Boolean) {
        viewStates = viewStates.copy(
            isShowTripsDialog = isShow
        )
    }


}

data class QianYanPlayViewState(
    val data: List<Advert> = emptyList(),
    val isShowTripsDialog: Boolean = false,
    val isLoading: Boolean = true,
)

sealed class QianYanPlayViewAction {
    object InitPageData : QianYanPlayViewAction()
    data class CheckIsVip(
        val isShowTripsDialog: Boolean = false
    ) : QianYanPlayViewAction()
}

sealed class QianYanPlayViewEvent {
    data class NavTo(val route: String) : QianYanPlayViewEvent()
    data class ShowMessage(val message: String) : QianYanPlayViewEvent()
}
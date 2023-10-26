package com.qianyanhuyu.app_large.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.model.AdvertType
import com.qianyanhuyu.app_large.data.model.AdvertTypeRequest
import com.qianyanhuyu.app_large.util.requestFlowResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : description
 */
@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val contentApi: ContentApi
) : ViewModel() {

    var viewStates by mutableStateOf(HomePageViewState())
        private set

    private val _viewEvents = Channel<HomePageViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(HomePageViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: HomePageViewAction) {
        when (action) {
            is HomePageViewAction.InitPageData -> initPageData()
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertList(
                        AdvertTypeRequest.PadHome.value,
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

data class HomePageViewState(
    val isLoading: Boolean = false,
    val data: List<Advert> = emptyList()
)

sealed class HomePageViewAction {
    object HomePage : HomePageViewAction()

    object InitPageData : HomePageViewAction()
}

sealed class HomePageViewEvent {
    data class NavTo(val route: String) : HomePageViewEvent()
    data class ShowMessage(val message: String) : HomePageViewEvent()
}
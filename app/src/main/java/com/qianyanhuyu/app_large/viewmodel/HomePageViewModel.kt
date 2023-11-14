package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.App.Companion.context
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.util.OtherAppUtil
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
 * @Description : 首页
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
            is HomePageViewAction.OpenViewContent -> openViewContent(action.url)
            else -> {

            }
        }
    }

    private fun initPageData() {
        viewModelScope.launch(exception) {
            // 由于不是从接口获取页数,所以写死3页,异步加载
            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertPage(
                        0,
                        3
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this?.content ?: emptyList()
                viewStates = viewStates.copy(
                    dataPage1 = data,
                )
            }

            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertPage(
                        1,
                        4
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this?.content ?: emptyList()
                viewStates = viewStates.copy(
                    dataPage2 = data,
                )
            }

            requestFlowResponse(
                requestCall = {
                    contentApi.getAdvertPage(
                        2,
                        3
                    )
                },
                showLoading = {
                    viewStates = viewStates.copy(
                        isLoading = it
                    )
                }
            ).apply {
                val data = this?.content ?: emptyList()
                viewStates = viewStates.copy(
                    dataPage3 = data,
                )
            }

        }
    }

    private fun openViewContent(
        url: String = ""
    ) {
        viewModelScope.launch {
            OtherAppUtil.openSystemVideo(
                url = url,
                context = context
            )
        }
    }
}

data class HomePageViewState(
    val isLoading: Boolean = false,
    val dataPage1: List<Advert> = emptyList(),
    val dataPage2: List<Advert> = emptyList(),
    val dataPage3: List<Advert> = emptyList()
)

sealed class HomePageViewAction {

    object InitPageData : HomePageViewAction()

    data class OpenViewContent(
        val url: String = ""
    ) : HomePageViewAction()
}

sealed class HomePageViewEvent {
    data class NavTo(val route: String) : HomePageViewEvent()
    data class ShowMessage(val message: String) : HomePageViewEvent()
}
package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 11:27
 * @Description : 旅游
 */
@HiltViewModel
class SmartTourismViewModel @Inject constructor(

) : ViewModel() {

    var viewStates by mutableStateOf(SmartTourismViewState())
        private set

    private val _viewEvents = Channel<SmartTourismViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: SmartTourismViewAction) {
        when (action) {
            is SmartTourismViewAction.InitPageData -> initPageData()
            else -> {

            }
        }
    }

    private fun initPageData() {
        // TODO 接口获取初始化旅游页内容,滑动更改数据类似实现可以参考干洗服务页面
    }

}

data class SmartTourismViewState(
    val isLoading: Boolean = true,
)

sealed class SmartTourismViewAction {
    object InitPageData : SmartTourismViewAction()
}

sealed class SmartTourismViewEvent {
    data class NavTo(val route: String) : SmartTourismViewEvent()
    data class ShowMessage(val message: String) : SmartTourismViewEvent()
}
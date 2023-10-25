package com.qianyanhuyu.app_large.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : description
 */
@HiltViewModel
class HomePageViewModel @Inject constructor(
) : ViewModel() {

    var viewStates by mutableStateOf(HomePageViewState())
        private set

    private val _viewEvents = Channel<HomePageViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()


    /**
     * 检查登录状态
     */
    private fun checkHomePageState() {
    }

    fun dispatch(action: HomePageViewAction) {
        when (action) {
            else -> {

            }
        }
    }
}

data class HomePageViewState(
    val isLogging: Boolean = true,
)

sealed class HomePageViewAction {
    object HomePage : HomePageViewAction()
}

sealed class HomePageViewEvent {
    data class NavTo(val route: String) : HomePageViewEvent()
    data class ShowMessage(val message: String) : HomePageViewEvent()
}
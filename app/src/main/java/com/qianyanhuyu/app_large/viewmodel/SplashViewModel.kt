package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.ui.common.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : 引导页逻辑代码
 */
@HiltViewModel
class SplashViewModel @Inject constructor(

) : ViewModel() {


    var viewStates by mutableStateOf(SplashViewState())
        private set

    private val _viewEvents = Channel<SplashViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: SplashViewAction) {
        when (action) {
            is SplashViewAction.CheckLoginState -> checkLoginState()
            else -> {

            }
        }
    }

    private fun checkLoginState() {
        println("收到错误消息：check")
        viewModelScope.launch {
            viewStates = viewStates.copy(isLogging = false)
            _viewEvents.send(SplashViewEvent.NavTo(Route.HOME_PAGE))
        }
    }

}

data class SplashViewState(
    val isLogging: Boolean = true
)

sealed class SplashViewAction {
    object Splash : SplashViewAction()

    object CheckLoginState: SplashViewAction()
}

sealed class SplashViewEvent {
    data class NavTo(val route: String) : SplashViewEvent()
    data class ShowMessage(val message: String) : SplashViewEvent()
}
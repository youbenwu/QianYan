package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.data.user.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : description
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userApi: UserApi
) : ViewModel() {

    var viewStates by mutableStateOf(LoginViewState())
        private set

    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.CheckLoginState -> checkLoginState()
            else -> {

            }
        }
    }

    /**
     * 检查登录状态
     */
    private fun checkLoginState() {

        val exception = CoroutineExceptionHandler { _, throwable ->
            val exception = CoroutineExceptionHandler { _, throwable ->
                viewStates = viewStates.copy(isLogging = false)
                viewModelScope.launch {
                    _viewEvents.send(LoginViewEvent.ShowMessage("请求失败：${throwable.message}"))
                }
            }
        }

        viewModelScope.launch(exception) {
            viewStates = viewStates.copy(
                isLogging = false
            )
        }
    }
}

data class LoginViewState(
    val email: String = "",
    val isLogging: Boolean = true,
)

sealed class LoginViewAction {
    object Login : LoginViewAction()
    object CheckLoginState: LoginViewAction()
}

sealed class LoginViewEvent {
    data class NavTo(val route: String) : LoginViewEvent()
    data class ShowMessage(val message: String) : LoginViewEvent()
}
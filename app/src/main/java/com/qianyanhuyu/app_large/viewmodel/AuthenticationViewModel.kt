package com.qianyanhuyu.app_large.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.ui.common.Route
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
class AuthenticationViewModel @Inject constructor(

) : ViewModel() {

    var viewStates by mutableStateOf(AuthenticationViewState())
        private set

    private val _viewEvents = Channel<AuthenticationViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(AuthenticationViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: AuthenticationViewAction) {
        when (action) {
            is AuthenticationViewAction.InitPageData -> initPageData()
            is AuthenticationViewAction.UpdatePersonNum -> updatePersonNum(action.personNum)
            is AuthenticationViewAction.ClearPersonNum -> clearPersonNum()
            is AuthenticationViewAction.UpdatePhone -> updatePhone(action.personPhone)
            is AuthenticationViewAction.ClearPhone -> clearPhone()
            is AuthenticationViewAction.UpdateVerificationCode -> updateVerificationCode(action.verificationCode)
            is AuthenticationViewAction.ClearVerificationCode -> clearVerificationCode()
            is AuthenticationViewAction.ConfirmAuthInfo -> confirmAuthInfo(action.verificationCode)
            else -> {

            }
        }
    }

    /**
     * 做一些进入页面初始化的操作
     */
    private fun initPageData() {
        viewModelScope.launch(exception) {
            // var userResponse: BaseResponse<User>?

            try {
                /*val response = userApi.getUser("test")
                if(response.isSuccessful && response.body() != null) {
                    userResponse = response.body()
                    // _viewEvents.send(AuthenticationViewEvent.ShowMessage("获取信息失败："+ userResponse?.data.toString()))
                } else {

                    return@launch
                }*/

            } catch(e: Exception) {
                return@launch
            }
        }
    }

    /**
     * 提交验证信息
     */
    private fun confirmAuthInfo(verificationCode: String) {
        Log.d("Auth -> VerificationCode: ", verificationCode)
        viewModelScope.launch {
            _viewEvents.send(AuthenticationViewEvent.NavTo(Route.ACTIVATION))
        }

    }

    private fun updatePersonNum(personNum: String) {
        viewStates = viewStates.copy(
            personNum = personNum
        )
    }

    private fun clearPersonNum() {
        viewStates = viewStates.copy(
            personNum = ""
        )
    }

    private fun clearPhone() {
        viewStates = viewStates.copy(
            personPhone = ""
        )
    }

    private fun updatePhone(personPhone: String) {
        viewStates = viewStates.copy(
            personPhone = personPhone
        )
    }

    private fun clearVerificationCode() {
        viewStates = viewStates.copy(
            verificationCode = ""
        )
    }

    private fun updateVerificationCode(verificationCode: String) {
        viewStates = viewStates.copy(
            verificationCode = verificationCode
        )
    }

}

data class AuthenticationViewState(
    val personNum: String = "",
    val personPhone: String = "",
    val verificationCode: String = "",
    val isLoading: Boolean = true,
)

sealed class AuthenticationViewAction {
    data class UpdatePhone(val personPhone: String) : AuthenticationViewAction()
    object ClearPhone: AuthenticationViewAction()
    data class UpdatePersonNum(val personNum: String) : AuthenticationViewAction()
    object ClearPersonNum: AuthenticationViewAction()
    object ClearVerificationCode: AuthenticationViewAction()
    data class UpdateVerificationCode(val verificationCode: String) : AuthenticationViewAction()

    data class ConfirmAuthInfo(val verificationCode: String) : AuthenticationViewAction()

    object InitPageData : AuthenticationViewAction()
}

sealed class AuthenticationViewEvent {
    data class NavTo(val route: String) : AuthenticationViewEvent()
    data class ShowMessage(val message: String) : AuthenticationViewEvent()
}
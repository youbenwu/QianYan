package com.qianyanhuyu.app_large.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.data.model.ApiResult
import com.qianyanhuyu.app_large.data.user.UserApi
import com.qianyanhuyu.app_large.data.user.model.SecurityUser
import com.qianyanhuyu.app_large.data.user.model.User
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.util.datastore.DataKey
import com.qianyanhuyu.app_large.util.datastore.DataStoreUtils
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
    private val userApi: UserApi
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
            var userResponse: ApiResult<User>?

            try {
                val response = userApi.getUser("388")
                if(response.isSuccessful && response.body() != null) {
                    userResponse = response.body()
                    // _viewEvents.send(AuthenticationViewEvent.ShowMessage("获取信息失败："+ userResponse?.data.toString()))
                } else {

                    return@launch
                }
            } catch(e: Exception) {
                return@launch
            }
//            viewStates = viewStates.copy(
//                personPhone = user?.code ?: ""
//            )
        }
    }

    /**
     * 提交验证信息
     */
    private fun confirmAuthInfo(a: String) {

        viewModelScope.launch {
            try {
                if(viewStates.personPhone==null||viewStates.personPhone.trim().isEmpty()){
                    _viewEvents.send(AuthenticationViewEvent.ShowMessage("请输入手机号码"))
                    return@launch
                }
                if(viewStates.verificationCode==null||viewStates.verificationCode.trim().isEmpty()){
                    _viewEvents.send(AuthenticationViewEvent.ShowMessage("请输入验证码"))
                    return@launch
                }
                val response = userApi.login(viewStates.personPhone,viewStates.verificationCode)
                if(response.isSuccessful && response.body() != null) {
                    var result:ApiResult<SecurityUser>? = response.body()
                    if(result?.status==0){

                        DataStoreUtils.putData(DataKey.userId,result.data.id);
                        DataStoreUtils.putData(DataKey.username,result.data.username);
                        DataStoreUtils.putData(DataKey.token,result.data.session?.token);
                        _viewEvents.send(AuthenticationViewEvent.NavTo(Route.ACTIVATION))
                        _viewEvents.send(AuthenticationViewEvent.ShowMessage("登陆成功"))
                    }else{
                        _viewEvents.send(AuthenticationViewEvent.ShowMessage("登陆失败："+ result?.message))
                    }
                } else {
                    _viewEvents.send(AuthenticationViewEvent.ShowMessage("请求失败"))
                    return@launch
                }

            } catch(e: Exception) {
                e.printStackTrace();
                return@launch
            }

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
package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.qianyanhuyu.app_large.data.model.MediaData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : description
 */
@HiltViewModel
class CustomerServiceViewModel @Inject constructor(

) : ViewModel() {

    var viewStates by mutableStateOf(CustomerServiceViewState())
        private set

    private val _viewEvents = Channel<CustomerServiceViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()


    init {
    }

    /**
     * 检查登录状态
     */
    private fun checkCustomerServiceState() {
    }

    fun dispatch(action: CustomerServiceViewAction) {
        when (action) {

            else -> {

            }
        }
    }


}

data class CustomerServiceViewState(
    val email: String = "",
    val mediaData: MediaData = MediaData(
        bannerSrc = "https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png"
    ),
    val isLogging: Boolean = true,
)

sealed class CustomerServiceViewAction {
    object CustomerService : CustomerServiceViewAction()
}

sealed class CustomerServiceViewEvent {
    data class NavTo(val route: String) : CustomerServiceViewEvent()
    data class ShowMessage(val message: String) : CustomerServiceViewEvent()
}
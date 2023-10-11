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
 * @CreateDate : 2023/9/25 11:27
 * @Description : Ip投放
 */
@HiltViewModel
class IpPutInViewModel @Inject constructor(

) : ViewModel() {

    var viewStates by mutableStateOf(IpPutInViewState())
        private set

    private val _viewEvents = Channel<IpPutInViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: IpPutInViewAction) {
        when (action) {

            else -> {

            }
        }
    }


}

data class IpPutInViewState(
    val email: String = "",
    val mediaData: MediaData = MediaData(
        bannerSrc = "https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png"
    ),
    val isLogging: Boolean = true,
)

sealed class IpPutInViewAction {
    object IpPutIn : IpPutInViewAction()
}

sealed class IpPutInViewEvent {
    data class NavTo(val route: String) : IpPutInViewEvent()
    data class ShowMessage(val message: String) : IpPutInViewEvent()
}
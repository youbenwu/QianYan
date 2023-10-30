package com.qianyanhuyu.app_large.viewmodel

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
 * @CreateDate : 2023/9/20 10:39
 * @Description : description
 */
@HiltViewModel
class ActivationViewModel @Inject constructor(
) : ViewModel() {
    var viewStates by mutableStateOf(ActivationViewState())
        private set

    private val _viewEvents = Channel<ActivationViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(ActivationViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: ActivationViewAction) {
        when (action) {
            is ActivationViewAction.InitPageData -> initPageData()
            is ActivationViewAction.UpdateEditText -> updateEditText(action.editTextType, action.text)
            is ActivationViewAction.ClearEditText -> clearEditText(action.editTextType)
            is ActivationViewAction.ConfirmActivationInfo -> confirmActivationInfo()
            else -> {

            }
        }
    }

    /**
     * 做一些进入页面初始化的操作
     */
    private fun initPageData() {
    }

    /**
     * 提交按钮事件
     */
    private fun confirmActivationInfo() {
        viewModelScope.launch {
            _viewEvents.send(ActivationViewEvent.NavTo(Route.HOME_GRAPH))
        }
    }

    private suspend fun navToHome() {
        _viewEvents.send(ActivationViewEvent.NavTo(Route.HOME_PAGE))
    }

    private fun clearEditText(type: ActivationEditTextType) {
        when(type) {
            ActivationEditTextType.DeviceId -> {
                viewStates = viewStates.copy(
                    deviceId = ""
                )
            }
            ActivationEditTextType.Location -> {
                viewStates = viewStates.copy(
                    location = ""
                )
            }
            ActivationEditTextType.RoomNum -> {
                viewStates = viewStates.copy(
                    roomNum = ""
                )
            }
            ActivationEditTextType.PersonName -> {
                viewStates = viewStates.copy(
                    personName = ""
                )
            }
        }
    }

    private fun updateEditText(type: ActivationEditTextType, text: String) {
        when(type) {
            ActivationEditTextType.DeviceId -> {
                viewStates = viewStates.copy(
                    deviceId = text
                )
            }
            ActivationEditTextType.Location -> {
                viewStates = viewStates.copy(
                    location = text
                )
            }
            ActivationEditTextType.RoomNum -> {
                viewStates = viewStates.copy(
                    roomNum = text
                )
            }
            ActivationEditTextType.PersonName -> {
                viewStates = viewStates.copy(
                    personName = text
                )
            }
        }

    }
}

data class ActivationViewState(
    val deviceId: String = "",
    val location: String = "",
    val roomNum: String = "",
    val personName: String = "",
    val isLoading: Boolean = true,
)

sealed class ActivationViewAction {
    object InitPageData : ActivationViewAction()
    object ConfirmActivationInfo : ActivationViewAction()

    data class UpdateEditText(
        val editTextType: ActivationEditTextType,
        val text: String
    ) : ActivationViewAction()
    data class ClearEditText(
        val editTextType: ActivationEditTextType
    ): ActivationViewAction()
}

sealed class ActivationViewEvent {
    data class NavTo(val route: String) : ActivationViewEvent()
    data class ShowMessage(val message: String) : ActivationViewEvent()
}

enum class ActivationEditTextType {
    DeviceId,
    Location,
    RoomNum,
    PersonName
}
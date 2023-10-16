package com.qianyanhuyu.app_large.viewmodel

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.App
import com.qianyanhuyu.app_large.data.hotel.HotelApi
import com.qianyanhuyu.app_large.data.hotel.model.Device
import com.qianyanhuyu.app_large.data.hotel.model.RegisterDeviceDTO
import com.qianyanhuyu.app_large.data.model.Address
import com.qianyanhuyu.app_large.data.model.ApiResult
import com.qianyanhuyu.app_large.data.user.UserApi
import com.qianyanhuyu.app_large.data.user.model.SecurityUser
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.util.datastore.DataKey
import com.qianyanhuyu.app_large.util.datastore.DataStoreUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/20 10:39
 * @Description : description
 */
@HiltViewModel
class ActivationViewModel @Inject constructor(
    private val hotelApi: HotelApi,
    private val userApi: UserApi
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
        viewStates = viewStates.copy(
            deviceId = getDeviceId()
        )
    }

    fun getDeviceId(): String {
        val telephonyManager = App.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId;
    }

    /**
     * 提交按钮事件
     */
    private fun confirmActivationInfo() {
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    if(viewStates.deviceId.trim().isEmpty()){
                        _viewEvents.send(ActivationViewEvent.ShowMessage("设备号不能为空"))
                        return@launch
                    }
                    if(viewStates.location.trim().isEmpty()){
                        _viewEvents.send(ActivationViewEvent.ShowMessage("请输入酒店名称"))
                        return@launch
                    }
                    if(viewStates.roomNum.trim().isEmpty()){
                        _viewEvents.send(ActivationViewEvent.ShowMessage("请输入房间号"))
                        return@launch
                    }
                    if(viewStates.personName.trim().isEmpty()){
                        _viewEvents.send(ActivationViewEvent.ShowMessage("请输入管理员姓名"))
                        return@launch
                    }
                    var userId:Int=DataStoreUtils.getData(DataKey.userId,0).last();
                    var phone:String?=DataStoreUtils.getData(DataKey.username,"").last();

                    var request:RegisterDeviceDTO=RegisterDeviceDTO(
                        userId=userId,
                        deviceNo=viewStates.deviceId,
                        roomNo = viewStates.roomNum,
                        hotelName = viewStates.location,
                        phone = phone,
                        name = viewStates.personName,
                        address = Address(
                            latitude = null,
                            longitude = null,
                            province = null,
                            city ="广州",
                            district = null,
                            street = null,
                            details = null,
                            fullAddress = null),
                    );




                    val response = hotelApi.registerDevice(request)
                    if(response.isSuccessful && response.body() != null) {
                        var result: ApiResult<Device>? = response.body()
                        if(result?.status==0){
                            var r:Boolean=loginDevice(viewStates.deviceId);
                            if(r){
                                _viewEvents.send(ActivationViewEvent.NavTo(Route.HOME_PAGE))
                                _viewEvents.send(ActivationViewEvent.ShowMessage("激活成功"))
                            }
                        }else{
                            _viewEvents.send(ActivationViewEvent.ShowMessage("激活失败："+ result?.message))
                        }
                    } else {
                        _viewEvents.send(ActivationViewEvent.ShowMessage("激活失败"))
                        return@launch
                    }

                } catch(e: Exception) {
                    e.printStackTrace();
                    return@launch
                }

            }
        }
    }

    private suspend fun loginDevice(deviceNo:String):Boolean{
        val response = userApi.login(deviceNo,"123456")
        if(response.isSuccessful && response.body() != null) {
            var result:ApiResult<SecurityUser>? = response.body()
            if(result?.status==0){
                DataStoreUtils.putData(DataKey.userId,result.data.id);
                DataStoreUtils.putData(DataKey.username,result.data.username);
                DataStoreUtils.putData(DataKey.token,result.data.session?.token);
                return getDeviceInfo(deviceNo);
            }else{
                _viewEvents.send(ActivationViewEvent.ShowMessage("登陆设备失败："+ result?.message))
                return false;
            }
        } else {
            _viewEvents.send(ActivationViewEvent.ShowMessage("登陆设备失败"))
            return false;
        }

        return true;
    }

    private suspend fun getDeviceInfo(deviceNo:String):Boolean{
        val response = hotelApi.getDevice(deviceNo)
        if(response.isSuccessful && response.body() != null) {
            var result:ApiResult<Device>? = response.body()
            if(result?.status==0){
                DataStoreUtils.putData(DataKey.deviceNo,result.data.deviceNo);
                DataStoreUtils.putData(DataKey.hotelId,result.data.hotelId);
            }else{
                _viewEvents.send(ActivationViewEvent.ShowMessage("获取设备信息失败："+ result?.message))
                return false;
            }
        } else {
            _viewEvents.send(ActivationViewEvent.ShowMessage("获取设备信息失败"))
            return false;
        }
        return true;
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
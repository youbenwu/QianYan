package com.qianyanhuyu.app_large.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.App.Companion.context
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.CIRCLE_NUMBER_IMAGE
import com.qianyanhuyu.app_large.constants.AppConfig.smallCircleRadius
import com.qianyanhuyu.app_large.model.ShopFriendsEditTextType
import com.qianyanhuyu.app_large.model.ShopFriendsForm
import com.qianyanhuyu.app_large.model.ShopFriendsImageData
import com.qianyanhuyu.app_large.ui.page.common.ShopFriendsAnimation
import com.qianyanhuyu.app_large.util.getImageBitmapByUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/25 11:27
 * @Description : 店友圈
 */
@HiltViewModel
class ShopFriendsViewModel @Inject constructor(

) : ViewModel() {

    var viewStates by mutableStateOf(ShopFriendsViewState())
        private set

    private val _viewEvents = Channel<ShopFriendsViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: ShopFriendsViewAction) {
        when (action) {
            is ShopFriendsViewAction.InitPageData -> initPageData()
            is ShopFriendsViewAction.UpdateImageData -> updateImageData()
            is ShopFriendsViewAction.UpdateOnlinePerson -> updateOnlinePerson()
            is ShopFriendsViewAction.IsShowCreateGroupChatDialog -> isShowCreateGroupChatDialog(action.isShowDialog)
            is ShopFriendsViewAction.UpdateFormValue -> updateFormValue(action.type, action.text)
            else -> {}
        }
    }

    /**
     * 图片
     */
    private fun initPageData() {

        viewModelScope.launch {

            val imageDataList = mutableListOf<ShopFriendsImageData>()

            val imageBitmap = context.getImageBitmapByUrl("https://img.js.design/assets/img/64c37ff60705af14a94fcad7.png#3a6702d289bd7f47bd984a6b91f14125")
            for (i in 0..2) {
                // 获取 0-360 度之间的随机度数
                val randomInt = generateSequence {
                    Random.nextInt(0, 360)
                }.filter { randomNum ->
                    return@filter randomNum % listOf(
                        50,
                        30,
                        70
                    ).shuffled().take(1)[0] == 0
                }
                .distinct()
                .take(CIRCLE_NUMBER_IMAGE + 1)
                .toList()

                for(index in 0 .. 4) {
                    imageDataList.add(
                        ShopFriendsImageData(
                            image = imageBitmap,
                            imageSize = smallCircleRadius.shuffled().take(1)[0],
                            name = "名称",
                            angle = randomInt[index],
                            offsetIndex = i,
                        ),
                    )
                }
            }

            imageDataList.add(
                ShopFriendsImageData(
                    image = imageBitmap,
                    imageSize = smallCircleRadius.shuffled().take(1)[0],
                    name = "名称",
                    offsetIndex = -1,
                ),
            )
            imageDataList.add(
                ShopFriendsImageData(
                    image = imageBitmap,
                    imageSize = smallCircleRadius.shuffled().take(1)[0],
                    name = "名称",
                    offsetIndex = -2,
                ),
            )

            viewStates = viewStates.copy(
                onlinePersonCount = 59116,
                formList = listOf(
                    ShopFriendsForm(
                        title = R.string.group_chat_form_name,
                        placeholder = R.string.group_chat_form_name_placeholder,
                        data = "",
                        type = ShopFriendsEditTextType.GroupName
                    ),
                    ShopFriendsForm(
                        title = R.string.group_chat_form_type,
                        placeholder = R.string.group_chat_form_type_placeholder,
                        data = "",
                        type = ShopFriendsEditTextType.GroupType
                    )
                ),
                imageData = viewStates.imageData.ifEmpty {
                    imageDataList.dropWhile {
                        it.isShow.value == ShopFriendsAnimation.Hide && it.isShowed
                    }.mapIndexed { index, shopFriendsImageData ->
                        shopFriendsImageData.copy(
                            imageDelay = index * 500
                        )
                    }
                },
                isLoading = false,
            )

        }
    }

    /**
     * 更新 Person 数据
     */
    private fun updateOnlinePerson() {
        val newOnlinePerson = viewStates.onlinePersonCount + (0..1000).shuffled().take(1)[0]

        // 59113 - 59221
        viewStates = viewStates.copy(
            onlinePersonCount = if(newOnlinePerson > 999999) 999999 else newOnlinePerson,
        )
    }

    /**
     * 更改照片的状态
     * 先显示所有照片
     * 再消失,再显示
     */
    private fun updateImageData() {

        if(viewStates.imageIsShowed) {
            val isHideCount = viewStates.imageData.count {
                it.isShow.value == ShopFriendsAnimation.Hide && !it.isShowed
            }

            if(isHideCount > 0) {
                addOnlinePerson()
            } else {
                deleteOnlinePerson()
            }
        } else {
            var flag = true
            viewStates = viewStates.copy(
                imageIsShowed = viewStates.imageData.count {
                    it.isShow.value == ShopFriendsAnimation.Display
                } == viewStates.imageData.size,
                imageData = viewStates.imageData.mapIndexed { _, shopFriendsImageData ->
                    if (flag && shopFriendsImageData.isShow.value == ShopFriendsAnimation.Hide) {
                        flag = false
                        shopFriendsImageData.apply {
                            isShow.value = ShopFriendsAnimation.Display
                        }
                    } else {
                        shopFriendsImageData
                    }
                }
            )
        }


    }

    private fun deleteOnlinePerson() {
        // 是否更改了元素的状态,每次执行只更改一条数据
        var flag = true
        viewStates = viewStates.copy(
            imageData = viewStates.imageData.mapIndexed { _, shopFriendsImageData ->
                if (flag && shopFriendsImageData.isShow.value == ShopFriendsAnimation.Display) {
                    flag = false

                    shopFriendsImageData.apply {
                        isShow.value = ShopFriendsAnimation.Hide
                    }.copy(
                        imageDelay = 0
                    )
                } else {
                    shopFriendsImageData
                }
            }
        )
    }

    private fun addOnlinePerson() {
        viewModelScope.launch {
            val imageBitmap = context.getImageBitmapByUrl("https://img.js.design/assets/img/64c7570e3fca9249265fbbe7.jpg#5ba3efbd60cdede3bd88763ee164a24a")

            var angle: Int = -1

            // 增加概率
            val circleIndex = listOf(
                0,1,2,
                -6,-5,-4,-3,-2,-1,
                0,1,2,
                0,1,2,
                0,1,2,
                0,1,2,
            ).shuffled().take(1)[0]

            // 获取 0-360 度之间的随机度数
            val randomInt = generateSequence {
                Random.nextInt(0, 360)
            }.filter { randomNum ->
                return@filter randomNum % listOf(
                    50,
                    30,
                    70
                ).shuffled().take(1)[0] == 0
            }
                .distinct()
                .take(CIRCLE_NUMBER_IMAGE + 3)
                .toList()

            randomInt.forEach { randomAngle ->
                val count = viewStates.imageData.count {
                    it.isShow.value == ShopFriendsAnimation.Display &&
                    ((circleIndex > 0 && it.offsetIndex == circleIndex && it.angle == randomAngle) ||
                    (circleIndex < 0 && it.offsetIndex == circleIndex ))
                }

                if(count == 0) {
                    angle = randomAngle
                    return@forEach
                }
            }

            val newImageData = viewStates.imageData.map {
                if(it.isShow.value == ShopFriendsAnimation.Hide) {
                    it.copy(
                        isShowed = true
                    )
                } else {
                    it
                }
            }.plus(
                ShopFriendsImageData(
                    image = imageBitmap,
                    imageSize = smallCircleRadius.shuffled().take(1)[0],
                    name = "名称",
                    angle = angle,
                    imageDelay = 2000,
                    offsetIndex = circleIndex,
                    isShow = mutableStateOf(ShopFriendsAnimation.Display)
                )
            )

            if(angle > -1) {
                viewStates = viewStates.copy(
                    imageData = newImageData
                )
            }
        }
    }

    private fun isShowCreateGroupChatDialog(isShow: Boolean) {
        viewStates = viewStates.copy(
            isShowDialog = isShow
        )
    }

    private fun updateFormValue(type: ShopFriendsEditTextType, data: String) {
        when(type) {
            ShopFriendsEditTextType.GroupTerm -> {
                viewStates = viewStates.copy(
                    termIsCheck = data.toBooleanStrict()
                )
            }
            ShopFriendsEditTextType.GroupType -> {
                // 在UTF-8下汉字占3个Byte
                val lengthMax = 18
                val isCheckLength = data.isNotEmpty() && data.toByteArray().size <= lengthMax

                viewStates = viewStates.copy(
                    formList = viewStates.formList.map {
                        if(it.type == type) {
                            it.copy(
                                data = if(data.isEmpty() || isCheckLength) {
                                    data
                                } else {
                                    it.data
                                }
                            )
                        } else {
                            it
                        }
                    }
                )
            }
            else -> {
                viewStates = viewStates.copy(
                    formList = viewStates.formList.map {
                        if(it.type == type) {
                            it.copy(
                                data = data
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }

}

data class ShopFriendsViewState(
    val imageData: List<ShopFriendsImageData> = mutableListOf(),
    val imageIsShowed: Boolean = false,
    val isLoading: Boolean = false,
    val onlinePersonCount: Int = 0,
    val isShowDialog: Boolean = false,
    val termIsCheck: Boolean = false,
    val formList: List<ShopFriendsForm> = mutableListOf()
)

sealed class ShopFriendsViewAction {
    object InitPageData : ShopFriendsViewAction()
    object UpdateImageData : ShopFriendsViewAction()
    object UpdateOnlinePerson: ShopFriendsViewAction()

    data class IsShowCreateGroupChatDialog(
        val isShowDialog: Boolean = false
    ): ShopFriendsViewAction()

    data class UpdateFormValue(
        val type: ShopFriendsEditTextType,
        val text: String = "",
    ): ShopFriendsViewAction()
}

sealed class ShopFriendsViewEvent {
    data class NavTo(val route: String) : ShopFriendsViewEvent()
    data class ShowMessage(val message: String) : ShopFriendsViewEvent()
}

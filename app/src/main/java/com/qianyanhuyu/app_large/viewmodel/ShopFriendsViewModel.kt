package com.qianyanhuyu.app_large.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.App.Companion.context
import com.qianyanhuyu.app_large.constants.AppConfig.CIRCLE_NUMBER_IMAGE
import com.qianyanhuyu.app_large.constants.AppConfig.smallCircleRadius
import com.qianyanhuyu.app_large.ui.page.common.ShopFriendsAnimation
import com.qianyanhuyu.app_large.util.cdp
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

}

data class ShopFriendsViewState(
    val imageData: List<ShopFriendsImageData> = mutableListOf(),
    val imageIsShowed: Boolean = false,
    val isLoading: Boolean = false,
    val onlinePersonCount: Int = 0,
)

sealed class ShopFriendsViewAction {
    object InitPageData : ShopFriendsViewAction()
    object UpdateImageData : ShopFriendsViewAction()
    object UpdateOnlinePerson: ShopFriendsViewAction()
}

sealed class ShopFriendsViewEvent {
    data class NavTo(val route: String) : ShopFriendsViewEvent()
    data class ShowMessage(val message: String) : ShopFriendsViewEvent()
}

/**
 * 头像数据类
 *
 * @param name 用户名称
 * @param image 头像 bitmap
 * @param imageSize 头像大小
 * @param isShow 是否显示, 默认 Hide
 * @param angle 角度
 * @param offsetIndex 位置在哪个圆上
 * @param isShowed 是否已经显示过了
 */
data class ShopFriendsImageData(
    val name: String = "",
    val image: Bitmap? = null,
    val imageSize: Dp = 0.cdp,
    val imageDelay: Int = 0,
    val isShow: MutableState<ShopFriendsAnimation> = mutableStateOf(ShopFriendsAnimation.Hide),
    val angle: Int = 0,
    val offsetIndex: Int = 0,
    val isShowed:Boolean = false,
)
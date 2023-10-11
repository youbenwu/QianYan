package com.qianyanhuyu.app_large.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qianyanhuyu.app_large.App.Companion.context
import com.qianyanhuyu.app_large.constants.AppConfig.smallCircleRadius
import com.qianyanhuyu.app_large.ui.page.circleImageCount
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
        }
    }

    /**
     * 图片
     */
    private fun initPageData() {

        viewModelScope.launch {
            // 角度封装
            val randomInts = mutableListOf<List<Int>>()
            val imageDataList = mutableListOf<ShopFriendsImageData>()

            val imageBitmap = context.getImageBitmapByUrl("https://img.js.design/assets/img/64c238082cf109c42e7591f8.png#57e917dbec79af945fed36c3a700c86e")
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
                .take(circleImageCount)
                .toList()

                randomInts.add(randomInt)

                // 5 个一组随机拿角度
                /*if (i != 0) {
                    continue
                }*/
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
                    angle = 50,
                    offsetIndex = -1,
                ),
            )
            imageDataList.add(
                ShopFriendsImageData(
                    image = imageBitmap,
                    imageSize = smallCircleRadius.shuffled().take(1)[0],
                    name = "名称",
                    angle = 50,
                    offsetIndex = -2,
                ),
            )

            viewStates = viewStates.copy(
                onlinePersonCount = 59116,
                imageData = viewStates.imageData.ifEmpty {
                    imageDataList
                },
                randomInts = randomInts,
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
     * 消失一个或两个,再显示其它一个或两个
     */
    private fun updateImageData() {
        viewStates = viewStates.copy(
            imageData = viewStates.imageData.mapIndexed { index, shopFriendsImageData ->
                if (shopFriendsImageData.isShow.value == ShopFriendsAnimation.Display) {
                    shopFriendsImageData.apply {
                        isShow.value = ShopFriendsAnimation.Hide
                    }.copy(
                        imageDelay = 1000
                    )
                } else {
                    shopFriendsImageData
                }
            }
        )
    }

}

data class ShopFriendsViewState(
    val imageData: List<ShopFriendsImageData> = mutableListOf(),
    val randomInts: MutableList<List<Int>> = mutableListOf(),
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
 * @param offset 位置
 * @param isShow 是否显示, 默认 Hide
 * @param changeFirst 是否第一次更改状态
 * @param angle 角度
 * @param offsetIndex 位置在哪个圆上
 */
data class ShopFriendsImageData(
    val name: String = "",
    val image: Bitmap? = null,
    val imageSize: Dp = 0.cdp,
    val imageDelay: Int = 0,
    val offset: IntOffset = IntOffset.Zero,
    val isShow: MutableState<ShopFriendsAnimation> = mutableStateOf(ShopFriendsAnimation.Hide),
    val angle: Int = 0,
    val offsetIndex: Int = 0,
)
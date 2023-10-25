package com.qianyanhuyu.app_large.model

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import com.qianyanhuyu.app_large.ui.page.common.ShopFriendsAnimation
import com.qianyanhuyu.app_large.util.cdp

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
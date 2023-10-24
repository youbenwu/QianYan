package com.qianyanhuyu.app_large.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/19 11:21
 * @Description : 通用菜单
 */

/**
 * FloatingActionButton填充的数据
 */
data class MultiMenuItem(
    val index: Int,
    @DrawableRes val icon: Int,
    val label: String,
    val labelTextColor: Color = Color.White,
    val route: String = "",
    val labelBackgroundColor: Color = Color.Transparent,
    val itemClick: () -> Unit = {}
)
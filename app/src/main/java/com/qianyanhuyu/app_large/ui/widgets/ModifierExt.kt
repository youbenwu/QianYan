package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/26 17:28
 * @Description : Modifier扩展
 */
fun Modifier.requestFocus(): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {}
}
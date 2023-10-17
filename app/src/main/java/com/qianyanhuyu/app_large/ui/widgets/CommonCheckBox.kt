package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.qianyanhuyu.app_large.constants.AppConfig

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/16 14:15
 * @Description : 通用Checkbox组件，更改事件通过组合项的去做
 */
@Composable
fun CommonCheckBox(
    isCheck: Boolean,
    checkedColor: Color = AppConfig.CustomBlue84,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = isCheck,
        onCheckedChange = null,
        colors = CheckboxDefaults.colors(
            checkedColor = checkedColor
        ),
        modifier = modifier
    )
}
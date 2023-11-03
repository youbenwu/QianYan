package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.util.cdp

/***
 * @Author : Cheng
 * @CreateDate : 2023/11/3 10:40
 * @Description : 圆形复选框
 */

@Composable
fun CircleCheckbox(
    selected: Boolean,
    enabled: Boolean = true,
    onChecked: () -> Unit
) {

    val color = Color(42, 138, 228)
    val imageVector = if (selected) Icons.Filled.CheckCircle else ImageVector.vectorResource(id = R.drawable.ic_circle_checkbox_unchecked)
    val tint = if (selected) color.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
    val background = if (selected) Color.White else Color.Transparent

    IconButton(
        onClick = { onChecked() },
        enabled = enabled,
    ) {

        Icon(imageVector = imageVector, tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox")
    }
}

@Preview
@Composable
fun PREVIEW () {
    CircleCheckbox(
        selected = false
    ) {}
}
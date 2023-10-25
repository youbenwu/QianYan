package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.TextUnit
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.theme.AuthButtonTextColor
import com.qianyanhuyu.app_large.ui.theme.ButtonColor
import com.qianyanhuyu.app_large.constants.AppConfig.CustomPurple
import com.qianyanhuyu.app_large.constants.AppConfig.originToBlueHorizontal
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/22 17:10
 * @Description : 通用按钮
 */
@Composable
fun CustomButton(
    text: String,
    textLetterSpacing: TextUnit= 4.csp,
    fontSize: TextUnit = 30.csp,
    shape: Shape = Shapes.small,
    contentColor: Color = Color.White,
    containerColor: Color = CustomPurple,
    isShowIcon: Boolean = false,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {

        Text(
            text,
            fontSize = fontSize,
            letterSpacing = textLetterSpacing,
        )

        AnimatedVisibility(
            visible = isShowIcon
        ) {
            CommonIcon(
                resId = R.drawable.ic_stop,
                tint = Color.White,
                modifier = Modifier
                    .size(30.cdp)
                    .padding(
                        start = 10.cdp
                    )
            )
        }
    }
}
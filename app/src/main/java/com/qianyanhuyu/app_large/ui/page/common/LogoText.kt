package com.qianyanhuyu.app_large.ui.page.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/25 14:13
 * @Description : 带Logo的Text
 */
@Composable
fun LogoText(
    @DrawableRes iconDrawable: Int,
    text: String = "",
    iconSize: Dp = 25.cdp,
    iconTint: Color = Color.White,
    fontSize: TextUnit = 30.csp,
    lineHeight: TextUnit = 35.csp,
    letterSpacing: TextUnit = 1.csp,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CommonIcon(
            resId = iconDrawable,
            tint = iconTint,
            modifier = Modifier
                .size(iconSize)
                .padding(
                    end = 4.cdp
                )
        )

        Text(
            text = text,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            letterSpacing = letterSpacing,
            color = Color.White,
            lineHeight = lineHeight
        )
    }
}
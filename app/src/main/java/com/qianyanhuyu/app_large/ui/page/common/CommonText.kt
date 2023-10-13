package com.qianyanhuyu.app_large.ui.page.common

import android.annotation.SuppressLint
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/13 15:29
 * @Description : 通用文字组件,设置统一状态
 */
@Composable
fun CommonText(
    text: String = "",
    fontSize: TextUnit = 30.csp,
    textAlign: TextAlign = TextAlign.Center,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    letterSpacing: TextUnit = 1.csp,
    color: Color = Color.White,
    lineHeight: TextUnit = TextUnit.Unspecified,
    style: TextStyle? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Text(
        text,
        fontSize = fontSize,
        textAlign = textAlign,
        maxLines = maxLines,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        overflow = overflow,
        color = color,
        style = style ?: LocalTextStyle.current,
        fontWeight = fontWeight,
        modifier = modifier
    )
}
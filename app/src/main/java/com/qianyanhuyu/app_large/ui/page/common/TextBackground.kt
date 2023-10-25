package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.CustomLavender
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/22 18:03
 * @Description : 带背景的文字Text
 */
@Composable
fun TextBackground(
    text: String,
    shapes: Shape = Shapes.extraSmall,
    textBackground: Color = CustomLavender,
    textBackgroundBrush: Brush? = null,
    fontSize: TextUnit = 25.csp,
    lineHeight: TextUnit = 30.csp,
    letterSpacing: TextUnit = 4.csp,
    textHorizontalPadding: Dp = 31.cdp,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    val modifierCurrent: Modifier = textBackgroundBrush?.let {
        modifier
            .background(
                textBackgroundBrush,
                shapes
            )
            .clip(shapes)
    } ?: modifier
            .background(
                textBackground,
                shapes
            )
            .clip(shapes)

    Box(
        modifier = modifierCurrent
            .onClick {
                onClick.invoke()
            }
    ) {
        CommonText(
            text,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            modifier = Modifier
                .padding(
                    vertical = 5.cdp,
                    horizontal = textHorizontalPadding
                )
        )
    }
}
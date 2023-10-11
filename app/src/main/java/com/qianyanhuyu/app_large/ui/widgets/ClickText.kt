package com.qianyanhuyu.app_large.ui.widgets

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.qianyanhuyu.app_large.ui.theme.BlackColor21
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/20 14:52
 * @Description : 可点击内容的Text,适用于隐私政策这一类
 */
@Composable
fun ActivationClickText(
    @StringRes text: Int,
    clickText: String,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.csp,
                letterSpacing = 2.csp,
                color = Color.White,
            )
        ) {
            append(stringResource(id = text))
        }
        pushStringAnnotation(
            tag = "tag1",
            annotation = "跳转地址：https://www.xxx1.com"
        )
        withStyle(
            style = SpanStyle(
                fontSize = 14.csp,
                fontWeight = FontWeight.Bold,
                color = BlackColor21,
            )
        ) {
            append(clickText)
        }
        pop()
    }

    val tags = listOf("tag1")
    ClickableText(
        text = annotatedText,
        onClick = { offset ->

            tags.forEach { tag ->
                annotatedText.getStringAnnotations(
                    tag = tag, start = offset,
                    end = offset
                )
                    .firstOrNull()?.let { annotation ->
                        Log.d("ClickText: ", annotation.item)
                        onClick.invoke()
                    }
            }
        },
        modifier = modifier
    )
}
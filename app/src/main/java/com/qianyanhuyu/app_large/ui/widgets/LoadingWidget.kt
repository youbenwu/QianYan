package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.qianyanhuyu.app_large.util.cdp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/19 16:18
 * @Description : 加载控件
 */

private const val TAG = "LoadingWidget"

@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier.fillMaxSize(),
    loading: Boolean = true,
    loadingWidth: Dp = 60.cdp,
    loadingHeight: Dp = 50.cdp,
    loadingRadius: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    contentAlignment: Alignment = Alignment.Center
) {
    val anim by remember {
        mutableStateOf(Animatable(0.4f))
    }

    LaunchedEffect(loading) {
        if (loading) {
            anim.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 450, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            anim.stop()
        }
    }


    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {

        Canvas(
            modifier = Modifier
                .width(loadingWidth)
                .height(loadingHeight)
        ) {
            val rectWidth = size.width / 7
            val canvasHeight = size.height

            val rectHeight1 = if (loading) {
                canvasHeight * (0.75f - anim.value * 0.75f + 0.25f)
            } else {
                canvasHeight * 0.7f
            }
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(if(loadingRadius) rectWidth / 2 else 0f),
                topLeft = Offset(0f, canvasHeight - rectHeight1),
                size = Size(rectWidth, rectHeight1)
            )

            val rectHeight2 = if (loading) {
                canvasHeight * (anim.value * 0.65f + 0.2f)
            } else {
                canvasHeight * 0.52f
            }
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(if(loadingRadius) rectWidth / 2 else 0f),
                topLeft = Offset(rectWidth * 2, canvasHeight - rectHeight2),
                size = Size(rectWidth, rectHeight2)
            )


            val rectHeight3 = if (loading) {
                canvasHeight * (0.6f - anim.value * 0.6f + 0.4f)
            } else {
                canvasHeight * 0.43f
            }
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(if(loadingRadius) rectWidth / 2 else 0f),
                topLeft = Offset(rectWidth * 4, canvasHeight - rectHeight3),
                size = Size(rectWidth, rectHeight3)
            )

            val rectHeight4 = if (loading) {
                //canvasHeight * (0.8f - anim.value * 0.8f + 0.2f)
                canvasHeight * (anim.value * 0.45f + 0.3f)
            } else {
                canvasHeight * 0.48f
            }
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(if(loadingRadius) rectWidth / 2 else 0f),
                topLeft = Offset(rectWidth * 6, canvasHeight - rectHeight4),
                size = Size(rectWidth, rectHeight4)
            )
        }
    }
}

package com.qianyanhuyu.app_large.ui.page.common

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toOffset
import com.qianyanhuyu.app_large.constants.AppConfig.CIRCLE_COUNT
import com.qianyanhuyu.app_large.util.BitmapUtil
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.inCircleOffset
import com.qianyanhuyu.app_large.util.toPx
import java.lang.Float.min

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/27 10:16
 * @Description : 店友圈的布局及动态效果
 */
private val baseColor = Color(88, 142, 233)

@Composable
fun ShopFriendsAnimation(
    modifier: Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            RingBackground(
                modifier.fillMaxSize()
            )
            ScanAnimation(modifier.fillMaxSize())
        }
    ) {measures, constraints ->

        val placeables = measures.mapIndexed { index, measurable ->
            val height = when (index) {
                0 -> constraints.maxHeight
                else -> constraints.maxHeight
            }

            measurable.measure(
                constraints.copy(
                    minWidth = 0,
                    minHeight = 0,
                    maxWidth = constraints.maxWidth,
                    maxHeight = height.toInt(),
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.place(x = 0, y = 0)
            }
        }
    }
}

/**
 * 圆环背景
 */
@Composable
private fun RingBackground(
    modifier: Modifier
) {

    val enable = remember {
        mutableStateOf(false)
    }
    val progress by animateFloatAsState(if (enable.value) 1f else 0f, animationSpec = tween(2000),
        label = ""
    )

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                enable.value = it.boundsInRoot().top >= 0 && it.boundsInRoot().right > 0
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)

        val simpleRadius = canvasHeight / 2

        val spacing = simpleRadius / CIRCLE_COUNT

        // 画圆
        for(i in 0..CIRCLE_COUNT) {
            // 圆的半径
            val radius = (simpleRadius - spacing * i) * progress

            drawCircle(
                color = baseColor.copy(alpha = 0.1f),
                center = center,
                radius = radius
            )
        }


    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ImageCircle(
    imgSrc: Bitmap?,
    name: String = "",
    delay: Int,
    circleImageState: MutableState<ShopFriendsAnimation> = mutableStateOf(ShopFriendsAnimation.Hide),
    circleSize: Dp = 0.cdp,
    circleAngle: Int = 0,
    circleOffsetIndex: Int = 0,
    modifier: Modifier
) {
    val enable = remember {
        mutableStateOf(false)
    }
    val progress by animateFloatAsState(if (enable.value) 1f else 0f, animationSpec = tween(2000, delay + 2000),
        label = ""
    )

    val transition = updateTransition(targetState = circleImageState, label = "")
    val startAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 2000, delay)
    }, label = "") { state ->
        if (state.value == ShopFriendsAnimation.Display) 1F else 0F
    }
    val scaleAnim: Float by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 2000, delay)
        },
        label = ""
    ) { state ->
        if(state.value == ShopFriendsAnimation.Display && startAnim == 1F) 1F else 0f
    }

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                enable.value = it.boundsInRoot().top >= 0 && it.boundsInRoot().right > 0
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // 圆外位置, 添加几个固定位置
        val outerCircles = listOf(
            IntOffset((canvasWidth * 0.1f).toInt(), (canvasHeight * 0.35f).toInt()),
            IntOffset(canvasWidth.toInt() - (canvasWidth * 0.12f).toInt(), (canvasHeight * 0.66f).toInt()),
            IntOffset(canvasWidth.toInt() - (canvasWidth * 0.14f).toInt(), (canvasHeight * 0.2f).toInt()),
            IntOffset(canvasWidth.toInt() - (canvasWidth * 0.18f).toInt(), (canvasHeight * 0.1f).toInt()),
            IntOffset((canvasWidth * 0.15f).toInt(), (canvasHeight * 0.2f).toInt()),
            IntOffset((canvasWidth * 0.2f).toInt(), (canvasHeight * 0.1f).toInt()),
        )

        val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)

        val simpleRadius = canvasHeight / 2

        val spacing = simpleRadius / CIRCLE_COUNT

        val imageSize = (circleSize.toPx * scaleAnim * progress).toInt()

        // 圆的半径
        val radius = simpleRadius - spacing * circleOffsetIndex

        imgSrc?.let { bitmap ->

            val offset = if(circleOffsetIndex >= 0) {
                inCircleOffset(center, radius, circleAngle)
            } else {
                outerCircles[0 - circleOffsetIndex - 1].toOffset()
            }

            //圆外画线
            if(circleOffsetIndex < 0 && circleImageState.value == ShopFriendsAnimation.Display) {
                drawLine(
                    color = Color(44, 71, 126),
                    start = Offset(
                        offset.x * scaleAnim * progress,
                        offset.y * scaleAnim * progress
                    ),
                    end = center,
                    alpha = scaleAnim * progress,
                    strokeWidth = 2.cdp.toPx() * scaleAnim * progress
                )
            }

            drawText(
                textMeasurer,
                name,
                size = Size(
                    width = imageSize.toFloat(),
                    height = imageSize / 2F
                ),
                topLeft = Offset(
                    offset.x - imageSize / 2,
                    offset.y - imageSize / 1.15F
                ),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = (imageSize / 3F).csp,
                    letterSpacing = 1.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            drawImage(
                BitmapUtil.getRoundedCornerBitmap(bitmap, if(imageSize > 50) imageSize else 50).asImageBitmap(),
                dstOffset =  IntOffset(
                    offset.x.toInt() - imageSize / 2,
                    offset.y.toInt() - imageSize / 2
                ),
                dstSize = IntSize(imageSize, imageSize),
            )
        }
    }
}

/**
 * 扫描线及动画效果
 */
@Composable
private fun ScanAnimation(
    modifier: Modifier
) {
    val enable = remember {
        mutableStateOf(false)
    }
    // 循环动画
    val trans = rememberInfiniteTransition(label = "line")
    // 从 0 - 360 度旋转动画
    val rote by trans.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(5000, 0, LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "line"
    )

    val transition = updateTransition(targetState = enable, label = "")
    val startAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 2000)
    }, label = "line") { state ->
        if (state.value) 1F else 0F
    }


    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                enable.value = it.boundsInRoot().top >= 0 && it.boundsInRoot().right > 0
            }
            .rotate(rote)
    ) {
        val realSize = min(size.width, size.height)
        drawCircle(
            Brush.sweepGradient(
                0f to Color.Transparent,
                0.05f to baseColor,
                0.051f to Color.Transparent,
            ),
            (realSize / 2) * startAnim
        )
    }
}

enum class ShopFriendsAnimation {
    Display,
    Hide
}
package com.qianyanhuyu.app_large.ui.common.indicator.header

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.qianyanhuyu.app_large.ui.common.indicator.ArrowDrawable
import com.qianyanhuyu.app_large.ui.common.swipe.RefreshType
import java.util.*
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.FormatterEnum
import com.qianyanhuyu.app_large.util.TimeUtil
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/**
 * 经典上拉加载更多footer
 */

val secondIconColor = Color.White
val secondTextColor = Color.White
@Composable
fun CommonRefreshHeader(refreshType: RefreshType) {

    val tip = when (refreshType) {
        RefreshType.PULL_TO_REFRESH -> "下拉刷新"
        RefreshType.RELEASE_TO_REFRESH -> "释放刷新"
        RefreshType.REFRESHING -> "正在刷新..."
        RefreshType.REFRESH_SUCCESS -> "刷新成功"
        RefreshType.REFRESH_FAIL -> "刷新失败"
        else -> "下拉刷新"
    }

    val angle = remember {
        Animatable(0f)
    }

    var lastRefreshTime by remember {
        mutableStateOf(TimeUtil.parse(Date().time, FormatterEnum.YYYY_MM_DD__HH_MM))
    }

    LaunchedEffect(refreshType) {
        when (refreshType) {
            RefreshType.PULL_TO_REFRESH -> {
                angle.animateTo(180f)
            }
            RefreshType.RELEASE_TO_REFRESH -> {
                angle.animateTo(0f)
            }
            RefreshType.REFRESH_SUCCESS -> {
                lastRefreshTime = TimeUtil.parse(Date().time, FormatterEnum.YYYY_MM_DD__HH_MM)
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.cdp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (refreshType == RefreshType.REFRESHING) {
                LoadingComponent(
                    modifier = Modifier.wrapContentSize(),
                    loadingWidth = 36.cdp,
                    loadingHeight = 36.cdp,
                    color = secondIconColor
                )
            } else if (refreshType == RefreshType.REFRESH_SUCCESS || refreshType == RefreshType.REFRESH_FAIL) {
                Box(modifier = Modifier.size(36.cdp))
            } else {
                val nativeColorInt = secondIconColor.toArgb()
                val nativeColor = android.graphics.Color.argb(
                    nativeColorInt.alpha,
                    nativeColorInt.red,
                    nativeColorInt.blue,
                    nativeColorInt.green
                )
                Image(
                    painter = rememberDrawablePainter(ArrowDrawable().apply {
                        setColor(nativeColor)
                    }),
                    contentDescription = "",
                    modifier = Modifier
                        .size(36.cdp)
                        .rotate(angle.value)
                )
            }
            Column(
                modifier = Modifier.padding(32.cdp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tip,
                color = secondTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 32.csp,
                    modifier = Modifier
                        .wrapContentSize()
                        .clipToBounds()
                )
                Text(
                    text = "上次更新 $lastRefreshTime",
                color = secondTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 24.csp,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 4.cdp)
                )
            }
            Box(modifier = Modifier.size(36.cdp))
        }
    }
}

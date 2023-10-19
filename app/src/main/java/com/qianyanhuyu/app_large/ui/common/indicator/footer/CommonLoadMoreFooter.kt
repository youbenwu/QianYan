package com.qianyanhuyu.app_large.ui.common.indicator.footer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qianyanhuyu.app_large.ui.common.swipe.RefreshState
import com.qianyanhuyu.app_large.ui.common.swipe.RefreshType
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick

/**
 * Created by ssk on 2023/2/2.
 * Description-> 经典上拉加载更多footer
 */
val secondTextColor = Color.White

@Composable
fun CommonLoadMoreFooter(
    refreshState: RefreshState,
    loadMoreRetryBlock: () -> Unit
) {


    if (refreshState.noMoreData) {
        NoMoreDataFooter()
    } else {
        val refreshType = refreshState.type

        if (refreshType == RefreshType.LOAD_MORE_FAIL
            || refreshType == RefreshType.LOAD_MORE_SUCCESS
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.cdp)
                    .onClick(enableRipple = false) {
                        if (refreshType == RefreshType.LOAD_MORE_FAIL) {
                            loadMoreRetryBlock.invoke()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val tip = if (refreshType == RefreshType.LOAD_MORE_FAIL) "加载失败，点击重试" else "加载成功"
                Text(
                    text = tip,
                    color = secondTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 32.csp,
                    modifier = Modifier
                        .padding(32.cdp, 0.dp)
                        .wrapContentSize()
                        .clipToBounds()
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.cdp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LoadingComponent(
                    modifier = Modifier.wrapContentSize(),
                    loadingWidth = 36.cdp,
                    loadingHeight = 36.cdp,
                    color = secondTextColor
                )

                Text(
                    text = "正在加载更多",
                    color = secondTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 32.csp,
                    modifier = Modifier
                        .padding(32.cdp, 0.dp)
                        .wrapContentSize()
                        .clipToBounds()
                )
                Box(modifier = Modifier.size(36.cdp))
            }

        }
    }

}

/**
 * 没有更多数据footer
 */
@Composable
private fun NoMoreDataFooter() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.cdp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "--没有更多数据啦--",
            fontSize = 30.csp,
            color = secondTextColor
        )
    }
}


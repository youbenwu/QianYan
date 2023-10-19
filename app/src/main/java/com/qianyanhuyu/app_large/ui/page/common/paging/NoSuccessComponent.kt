package com.qianyanhuyu.app_large.ui.page.common.paging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon

/**
 * Created by ssk on 2022/4/17.
 */
@Composable
fun NoSuccessComponent(
    modifier: Modifier = Modifier.fillMaxSize(),
    loadDataBlock: (() -> Unit)? = null,
    specialRetryBlock: (() -> Unit)? = null,
    contentAlignment: Alignment = Alignment.Center,
    iconResId: Int = R.drawable.ic_empty,
    message: String = "加载失败"
) {
    val contentColor = Color(76, 155, 245)

    Box(
        modifier = modifier
            .clickable {
                if(specialRetryBlock != null) {
                    specialRetryBlock.invoke()
                }else {
                    loadDataBlock?.invoke()
                }
            },
        contentAlignment = contentAlignment
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CommonIcon(
                iconResId,
                tint = contentColor,
                modifier = Modifier.size(100.dp)
            )
            if (message.isNotEmpty()) {
                Text(
                    message,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}
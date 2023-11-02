package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.topTripsColor
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/22 18:45
 * @Description : 顶部提示信息栏
 */
@Composable
fun CustomTopTrips(
    text: String,
    isShowSearch: Boolean = false,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.large)
            .background(
                topTripsColor
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CommonIcon(
                R.drawable.ic_tips,
                tint = Color.White,
                modifier = Modifier
                    .padding(
                        start = 54.cdp,
                        end = 20.cdp
                    )
                    .size(38.cdp)
            )

            CommonText(
                text = text,
                fontSize = 32.csp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.csp,
                modifier = Modifier
                    .padding(
                        vertical = 15.cdp
                    )
                    .weight(1f)
            )

            AnimatedVisibility(visible = isShowSearch) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(
                            start = 20.cdp,
                            end = 54.cdp
                        )
                        .size(38.cdp)
                )
            }
        }
    }
}
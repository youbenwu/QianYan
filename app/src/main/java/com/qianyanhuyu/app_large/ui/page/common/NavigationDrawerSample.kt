package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.util.cdp

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/17 10:32
 * @Description : 侧边栏展开
 *
 * @param drawerState 侧边栏开启状态, 可通过 open(),close()方法操作,isOpen,isClose 获取当前状态
 * @param sheetContent 侧边栏内容
 * @param content 主体内容
 */
@ExperimentalMaterial3Api
@Composable
fun NavigationDrawerSample(
    drawerState: DrawerState,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    // 设置布局方向
    CompositionLocalProvider(
        LocalLayoutDirection.provides(LayoutDirection.Rtl)
    ) {

        val progress by animateFloatAsState(if (drawerState.isOpen) 0.33f else 0f, animationSpec = tween(200),
            label = ""
        )

        // 侧边栏菜单, gesturesEnabled控制是否可滑动唤出
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                // 侧边栏主背景显示, 背景渐变, 不需要圆角
                ModalDrawerSheet(
                    drawerContainerColor = Color.Transparent,
                    drawerContentColor = Color.Transparent,
                    modifier = Modifier
                        .then(
                            Modifier.fillMaxWidth(progress)
                        )
                        .background(
                            AppConfig.navDrawerBgBrush,
                            RoundedCornerShape(0.cdp)
                        )
                ) {
                    CompositionLocalProvider(
                        LocalLayoutDirection.provides(LayoutDirection.Ltr)
                    ){
                        // 侧边栏内容
                        sheetContent()
                    }
                }
            },
            modifier = Modifier
                .safeDrawingPadding()
        ) {
            CompositionLocalProvider(
                LocalLayoutDirection.provides(LayoutDirection.Ltr)
            ){
                // 主体内容
                content()
            }

        }
    }
}
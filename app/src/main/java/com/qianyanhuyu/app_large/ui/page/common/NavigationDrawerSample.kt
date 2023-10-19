package com.qianyanhuyu.app_large.ui.page.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
 */

@ExperimentalMaterial3Api
@Composable
fun NavigationDrawerSample(
    drawerState: DrawerState,
    sheetContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalLayoutDirection.provides(LayoutDirection.Rtl)
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled =/* drawerState.isOpen*/true,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color.Transparent,
                    drawerContentColor = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth(0.33f)
                        .background(
                            AppConfig.navDrawerBgBrush,
                            RoundedCornerShape(0.cdp)
                        )
                ) {
                    CompositionLocalProvider(
                        LocalLayoutDirection.provides(LayoutDirection.Ltr)
                    ){
                        sheetContent()
                    }
                }
            },
        ) {
            CompositionLocalProvider(
                LocalLayoutDirection.provides(LayoutDirection.Ltr)
            ){
                content()
            }

        }
    }
}
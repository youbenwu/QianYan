package com.qianyanhuyu.app_large.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route

/**
 * 处理状态栏情况
 */
@Composable
fun FixSystemBarsColor() {
    val sysUiController = rememberSystemUiController()

    val curRouteName = AppNavController.instance.currentBackStackEntryAsState().value?.destination?.route
    if (curRouteName == Route.SPLASH) {  // 闪屏页，状态栏透明，图标白色
        sysUiController.setSystemBarsColor(Color.Transparent, false)
    } else if (
        curRouteName == Route.HOME_PAGE ||
        curRouteName == Route.ACTIVATION ||
        curRouteName == Route.AUTHENTICATION ||
        curRouteName == Route.CUSTOMER_SERVICE
    ) {  // 主页面，状态栏透明
        sysUiController.setSystemBarsColor(Color.Transparent, false)
    } else {
        sysUiController.setSystemBarsColor(MaterialTheme.colorScheme.background)
    }


    sysUiController.setNavigationBarColor(MaterialTheme.colorScheme.background)
}
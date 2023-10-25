package com.qianyanhuyu.app_large.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.qianyanhuyu.app_large.util.cdp

object AppNavController {
    @SuppressLint("StaticFieldLeak")
    lateinit var instance: NavHostController

}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeNavHost(
    navController: NavHostController,
    startDestination: String = Route.SPLASH,
    route: String? = null,
    drawerState: DrawerState,
    onFinish: () -> Unit = {}
) {
    AppNavController.instance = navController

    AnimatedNavHost(
        route = route,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.SPLASH) {
            Column(Modifier.systemBarsPadding()) {
                SplashScreen()
            }

        }

        composable(Route.LOGIN) {
            LoginScreen()
        }

        // 认证页
        composable(Route.AUTHENTICATION) {
            AuthenticationScreen()
        }

        // 激活页
        composable(Route.ACTIVATION) {
            ActivationScreen()
        }

        homeGraph(
            route = Route.HOME_GRAPH,
            drawerState = drawerState,
        )

        // 客房服务
        /*composable(Route.CUSTOMER_SERVICE) {
            // Column(Modifier.systemBarsPadding()) {}
            CustomerServiceScreen()
        }*/
    }

}

/**
 * 需要保持头部的页面
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.homeGraph(
    startDestination: String = Route.HOME_CONTENT,
    drawerState: DrawerState,
    route: String,
) {
    navigation(
        startDestination = startDestination,
        route = route
    ) {
        composable(Route.HOME_CONTENT) {
            HomePageScreen()
        }

        composable(Route.CUSTOMER_SERVICE) {
            CustomerServiceScreen()
        }

        composable(Route.QIAN_YAN_PLAY) {
            QianYanPlayScreen()
        }

        composable(Route.QIAN_YAN_GIVE) {
            QianYanGiveScreen()
        }

        composable(Route.SHOP_FRIENDS) {
            ShopFriendsScreen(
                drawerState = drawerState
            )
        }

        composable(Route.IP_PUT_IN) {
            IpPutInScreen()
        }

        composable(Route.SMART_TOURISM) {
            SmartTourismScreen()
        }

        composable(Route.DRY_CLEAN) {
            DryClean()
        }
    }
}

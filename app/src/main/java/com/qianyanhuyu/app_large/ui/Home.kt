package com.qianyanhuyu.app_large.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

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
    snackHostState: SnackbarHostState? = null,
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

        // 认证页
        composable(Route.AUTHENTICATION) {
            AuthenticationScreen()
        }

        // 激活页
        composable(Route.ACTIVATION) {
            ActivationScreen()
        }

        // 首页及从首页进入的内容 systemBarsPadding
        homeGraph(
            route = Route.HOME_GRAPH,
            drawerState = drawerState,
            snackHostState = snackHostState,
        )
    }

}

/**
 * 需要保持头部的页面
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.homeGraph(
    startDestination: String = Route.HOME_CONTENT,
    drawerState: DrawerState,
    snackHostState: SnackbarHostState? = null,
    route: String,
) {
    navigation(
        startDestination = startDestination,
        route = route
    ) {
        composable(Route.HOME_CONTENT) {
            HomePageScreen(
                snackHostState = snackHostState
            )
        }

        composable(Route.CUSTOMER_SERVICE) {
            CustomerServiceScreen()
        }

        composable(Route.QIAN_YAN_PLAY) {
            QianYanPlayScreen(
                snackHostState = snackHostState
            )
        }

        composable(Route.QIAN_YAN_GIVE) {
            QianYanGiveScreen(
                snackHostState = snackHostState
            )
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

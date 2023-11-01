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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.qianyanhuyu.app_large.ui.page.common.WebViewPage

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

        /**
         * Url 传递的时候需要Encode
         * 例: URLEncoder.encode("url", StandardCharsets.UTF_8.toString())
         */
        composable(
            route = "${Route.WEB_VIEW}/{${Route.WEB_VIEW_TITLE}}/{${Route.WEB_VIEW_URL}}",
            arguments = listOf(
                navArgument(Route.WEB_VIEW_TITLE) {
                    type = NavType.StringType
                },
                navArgument(Route.WEB_VIEW_URL) {
                    type = NavType.StringType
                },
            )
        ) {
            val title = it.arguments?.getString("title")
            val url = it.arguments?.getString("url")

            WebViewPage(
                title = title,
                url = url
            )
        }
    }
}

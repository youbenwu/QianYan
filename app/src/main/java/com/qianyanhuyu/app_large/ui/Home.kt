package com.qianyanhuyu.app_large.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

object AppNavController {
    @SuppressLint("StaticFieldLeak")
    lateinit var instance: NavHostController

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavHost(
    navController: NavHostController,
    startDestination: String = Route.SPLASH,
    onFinish: () -> Unit = {}
) {
    AppNavController.instance = navController

    AnimatedNavHost(
        navController,
        startDestination
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

        // 首页
        composable(Route.HOME_PAGE) {
            HomePageScreen {
                onFinish()
            }
        }

        // 客房服务
        /*composable(Route.CUSTOMER_SERVICE) {
            // Column(Modifier.systemBarsPadding()) {}
            CustomerServiceScreen()
        }*/
    }

}
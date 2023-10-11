package com.qianyanhuyu.app_large.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.viewmodel.SplashViewAction
import com.qianyanhuyu.app_large.viewmodel.SplashViewEvent
import com.qianyanhuyu.app_large.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/18 9:34
 * @Description : 引导页
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        delay(1000)
        AppNavController.instance.popBackStack()
        AppNavController.instance.navigate(Route.AUTHENTICATION)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
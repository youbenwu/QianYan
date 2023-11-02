package com.qianyanhuyu.app_large.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.viewmodel.SplashViewAction
import com.qianyanhuyu.app_large.viewmodel.SplashViewEvent
import com.qianyanhuyu.app_large.viewmodel.SplashViewModel

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/18 9:34
 * @Description : 引导页
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(SplashViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is SplashViewEvent.NavTo) {
                AppNavController.instance.popBackStack()
                AppNavController.instance.navigate(it.route).apply {

                }
            }
            else if (it is SplashViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
            }
        }
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
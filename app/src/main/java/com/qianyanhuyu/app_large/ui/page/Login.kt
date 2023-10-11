package com.qianyanhuyu.app_large.ui.page

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.viewmodel.LoginViewAction
import com.qianyanhuyu.app_large.viewmodel.LoginViewEvent
import com.qianyanhuyu.app_large.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:19
 * @Description : 登录页面
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val activity = (LocalContext.current as? Activity)


    DisposableEffect(Unit) {
        viewModel.dispatch(LoginViewAction.CheckLoginState)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is LoginViewEvent.NavTo) {
                AppNavController.instance.popBackStack()
                AppNavController.instance.navigate(it.route) {
                    // popUpTo(0)
                }
            }
            else if (it is LoginViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
//                coroutineState.launch {
//                    scaffoldState.snackbarHostState.showSnackbar(message = it.message)
//                }
            }
        }
    }

    MaterialTheme {
        LoginContent()
    }
}

@Composable
fun LoginContent() {
    Text(
        text = "testaasssssss"
    )
}

@Preview
@Composable
fun previewContent() {
    MaterialTheme {
        LoginContent()
    }
}
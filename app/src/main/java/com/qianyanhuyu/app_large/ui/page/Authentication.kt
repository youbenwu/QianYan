package com.qianyanhuyu.app_large.ui.page

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.theme.AuthButtonTextColor
import com.qianyanhuyu.app_large.ui.theme.ButtonColor
import com.qianyanhuyu.app_large.ui.theme.IconColor
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.BaseMsgDialog
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.LoadDataContent
import com.qianyanhuyu.app_large.ui.widgets.SimpleEditTextWidget
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.AuthenticationViewAction
import com.qianyanhuyu.app_large.viewmodel.AuthenticationViewEvent
import com.qianyanhuyu.app_large.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 13:41
 * @Description : 认证页
 * 认证手机号码
 */

private const val TAG = "el, AuthenticationScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(AuthenticationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is AuthenticationViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route) {
                    popUpTo(0)
                }
            }
            else if (it is AuthenticationViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(0.cdp))
            }
        ) {innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AuthenticationContent(
                    modifier = Modifier
                )
            }
        }
    }
}

/**
 * 总体内容View
 */
@Composable
fun AuthenticationContent(
    modifier: Modifier
) {

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val(
            personInfoView,
            titleTextView,
            personInfoLeftView,
            personInfoRightView
        ) = createRefs()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

//            CommonLocalImage(
//                R.drawable.common_bg,
//                modifier = Modifier
//                    .fillMaxSize()
//            )
            CommonComposeImage(
                R.drawable.common_bg,
                modifier = Modifier.fillMaxSize()
            )

        }

        TitleWidget(
            Modifier
                .constrainAs(titleTextView) {
                    top.linkTo(parent.top, margin = 98.cdp)
                }
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .constrainAs(personInfoView) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = 70.cdp,
                        endMargin = 70.cdp
                    )
                    top.linkTo(titleTextView.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        ) {

            ConstraintLayout(
                modifier = Modifier.padding(
                    start = 206.cdp,
                    end = 206.cdp,
                    top = 60.cdp,
                    bottom = 115.cdp
                )
            ) {
                val(
                    icCheckBgRightTop,
                    icCheckBgLeftTop
                ) = createRefs()

                // 竖中线
                val guideLine = createGuidelineFromStart(0.5f)

                CommonIcon(
                    R.drawable.ic_check_bg_right_top,
                    tint = Color(50, 195, 211),
                    modifier = Modifier.constrainAs(icCheckBgRightTop) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                )

                CommonIcon(
                    R.drawable.ic_check_bg_left_top,
                    tint = Color(50, 195, 211),
                    modifier = Modifier.constrainAs(icCheckBgLeftTop) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                CommonLocalImage(
                    R.drawable.check_person_bg,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.cdp)
                        .alpha(0.5f)
                )

                CommonComposeImage(
                    R.drawable.check_person_right,
                    modifier = Modifier
                        .constrainAs(personInfoRightView) {
                            linkTo(start = parent.start, end = guideLine)
                            top.linkTo(parent.top)
                        }
                        .width(554.cdp)
                        .height(653.cdp)
                )

                AuthInfoWidget(
                    modifier = Modifier
                        .constrainAs(personInfoLeftView) {
                            linkTo(
                                start = guideLine,
                                end = parent.end,
                            )
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxWidth(0.5f)
                )
            }
        }


    }
}

/**
 * 头部文字视图
 */
@Composable
fun TitleWidget(
    modifier: Modifier
) {
    Text(
        text = stringResource(id = R.string.check_person_page_title),
        fontWeight = FontWeight.Bold,
        fontSize = 100.csp,
        textAlign = TextAlign.Center,
        letterSpacing = 4.csp,
        color = Color.White,
        modifier = modifier
    )
}

/**
 * 输入用户信息视图
 */
@Composable
fun AuthInfoWidget(
    modifier: Modifier
) {

    val loginViewModel: AuthenticationViewModel = viewModel()
    val viewState = loginViewModel.viewStates

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        SimpleEditTextWidget(
            value = viewState.personNum,
            valueLabel = R.string.check_person_num,
            leadingIcon = R.drawable.ic_person,
            onClick = {
                loginViewModel.dispatch(AuthenticationViewAction.ClearPersonNum)
            },
            onValueChange = {
                loginViewModel.dispatch(AuthenticationViewAction.UpdatePersonNum(it))
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 30.cdp)
        )
        SimpleEditTextWidget(
            value = viewState.personPhone,
            valueLabel = R.string.check_person_phone,
            leadingIcon = R.drawable.ic_phone,
            keyboardType = KeyboardType.Phone,
            onClick = {
                loginViewModel.dispatch(AuthenticationViewAction.ClearPhone)
            },
            onValueChange = {
                loginViewModel.dispatch(AuthenticationViewAction.UpdatePhone(it))
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 30.cdp)
        )
        SimpleEditTextWidget(
            value = viewState.verificationCode,
            valueLabel = R.string.check_person_verification_code,
            leadingIcon = R.drawable.ic_ver_code,
            onClick = {
                loginViewModel.dispatch(AuthenticationViewAction.ClearVerificationCode)
            },
            onValueChange = {
                loginViewModel.dispatch(AuthenticationViewAction.UpdateVerificationCode(it))
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 30.cdp)
        )
        ConfirmAuth(
            modifier = Modifier
                .padding(
                    top = 75.cdp
                )
        ){
            loginViewModel.dispatch(AuthenticationViewAction.ConfirmAuthInfo("test"))
        }
    }
}

@Composable
fun ConfirmAuth(
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            shape = Shapes.extraLarge,
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColor,
                contentColor = AuthButtonTextColor
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = stringResource(id = R.string.check_person_confirm_btn),
                fontSize = 30.csp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewContent() {
    AuthInfoWidget(Modifier
        .fillMaxWidth())

}
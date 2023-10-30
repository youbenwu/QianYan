package com.qianyanhuyu.app_large.ui.page

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.theme.BlackColor21
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.theme.TitleColor39
import com.qianyanhuyu.app_large.ui.widgets.ActivationClickText
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.SimpleEditTextWidget
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.ActivationViewAction
import com.qianyanhuyu.app_large.viewmodel.ActivationViewModel
import com.qianyanhuyu.app_large.viewmodel.ActivationEditTextType
import com.qianyanhuyu.app_large.viewmodel.ActivationViewEvent
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/20 10:38
 * @Description : description
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationScreen(
    viewModel: ActivationViewModel = hiltViewModel()
) {

    val snackHostState = remember { SnackbarHostState() }
    val coroutineState = rememberCoroutineScope()


    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is ActivationViewEvent.NavTo) {
                AppNavController.instance.popBackStack()
                AppNavController.instance.navigate(it.route)
            }
            else if (it is ActivationViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    MaterialTheme {
            Scaffold(
            snackbarHost = { SnackbarHost(snackHostState) },
            topBar = {
                Box(Modifier.fillMaxWidth())
            }
        ) {
            ActivationContent(
                modifier = Modifier.fillMaxSize()
                    .padding(it)
            )
        }
    }
}

/**
 * 总体内容View
 */
@Composable
fun ActivationContent(
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {

        val(
            activationInfoView,
            personImageView,
            rightTextView
        ) = createRefs()

        // 竖中线
        val guideLine = createGuidelineFromStart(0.5f)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonComposeImage(
                R.drawable.common_bg,
                modifier = Modifier.fillMaxSize()
            )

        }

        CommonComposeImage(
            R.drawable.person_image,
            modifier = Modifier
                .constrainAs(personImageView) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .fillMaxHeight(0.4f)
                .graphicsLayer {
                    this.translationX = 90.cdp.toPx()
                }
                .zIndex(999f)
        )

        Box(
            modifier = Modifier
                .constrainAs(activationInfoView) {
                    linkTo(
                        start = guideLine,
                        end = parent.end,
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                    )
                }
                .padding(
                    start = 80.cdp,
                    end = 190.cdp,
                    top = 78.cdp
                )
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
        ) {

            ActivationInfoWidget(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        RightTextWidget(
            modifier = Modifier
                .constrainAs(rightTextView) {
                    linkTo(
                        start = parent.start,
                        end = guideLine
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                }
                .padding(
                    horizontal = 200.cdp
                )
                .fillMaxWidth(0.5f)
        )

    }
}

/**
 * 输入用户信息视图
 */
@Composable
fun ActivationInfoWidget(
    modifier: Modifier
) {
    val activationViewModel: ActivationViewModel = viewModel()
    val viewState = activationViewModel.viewStates

    Box(
        modifier = modifier
    ) {
        CommonLocalImage(
            R.drawable.activation_info_bg,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 24.cdp,
                        topEnd = 24.cdp
                    )
                )
                .fillMaxSize()
        )
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (
                activationEditInfoWidget
            ) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(activationEditInfoWidget) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(
                        horizontal = 60.cdp,
                        vertical = 40.cdp
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.activation_info_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.csp,
                    textAlign = TextAlign.Left,
                    letterSpacing = 4.csp,
                    color = TitleColor39,
                    modifier = Modifier.fillMaxWidth()
                )
                SimpleEditTextWidget(
                    value = viewState.deviceId,
                    valueLabel = R.string.activation_device_id,
                    onClick = {
                        activationViewModel.dispatch(ActivationViewAction.ClearEditText(ActivationEditTextType.DeviceId))
                    },
                    onValueChange = {
                        activationViewModel.dispatch(ActivationViewAction.UpdateEditText(ActivationEditTextType.DeviceId, it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.cdp)
                )
                SimpleEditTextWidget(
                    value = viewState.location,
                    valueLabel = R.string.activation_location,
                    onClick = {
                        activationViewModel.dispatch(ActivationViewAction.ClearEditText(ActivationEditTextType.Location))
                    },
                    onValueChange = {
                        activationViewModel.dispatch(ActivationViewAction.UpdateEditText(ActivationEditTextType.Location, it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.cdp)
                )
                SimpleEditTextWidget(
                    value = viewState.roomNum,
                    valueLabel = R.string.activation_room_num,
                    onClick = {
                        activationViewModel.dispatch(ActivationViewAction.ClearEditText(ActivationEditTextType.RoomNum))
                    },
                    onValueChange = {
                        activationViewModel.dispatch(ActivationViewAction.UpdateEditText(ActivationEditTextType.RoomNum, it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.cdp)
                )
                SimpleEditTextWidget(
                    value = viewState.personName,
                    valueLabel = R.string.activation_person_name,
                    onClick = {
                        activationViewModel.dispatch(ActivationViewAction.ClearEditText(ActivationEditTextType.PersonName))
                    },
                    onValueChange = {
                        activationViewModel.dispatch(ActivationViewAction.UpdateEditText(ActivationEditTextType.PersonName, it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.cdp)
                )
                Button(
                    onClick = {
                        activationViewModel.dispatch(ActivationViewAction.ConfirmActivationInfo)
                    },
                    shape = Shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlackColor21,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(
                            top = 50.cdp
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.activation_confirm_text),
                        fontSize = 24.csp,
                        fontWeight = FontWeight.Bold
                    )
                }
                ActivationClickText(
                    text = R.string.activation_confirm_tips,
                    clickText = "请点击此处",
                    modifier = Modifier
                        .padding(
                            start = 20.cdp,
                            top = 6.cdp
                        )
                ) {
                    Log.d("ClickText: ", "ActivationClickText")
                }
            }
        }
    }
}

/**
 * 左侧文本内容
 */
@Composable
fun RightTextWidget(
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.activation_msg_title),
                fontWeight = FontWeight.Bold,
                fontSize = 50.csp,
                textAlign = TextAlign.Left,
                letterSpacing = 4.csp,
                color = TitleColor39
            )
            CommonComposeImage(
                resId = R.drawable.ic_good,
                modifier = Modifier.padding(
                    start = 20.cdp
                )
            )
        }
        RightTextCommon(
            R.string.activation_msg_1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 160.cdp)
        )
        RightTextCommon(
            R.string.activation_msg_2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.cdp)
        )
        RightTextCommon(
            R.string.activation_msg_3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.cdp)
        )

    }
}

@Composable
fun RightTextCommon(
    @StringRes text: Int,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CommonIcon(
            resId = R.drawable.ic_check,
            tint = Color(244, 234, 42),
            modifier = Modifier
                .padding(
                    end = 4.cdp
                )
                .width(33.cdp)
                .height(24.cdp)
        )
        Text(
            text = stringResource(id = text),
            fontSize = 24.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 2.csp,
            color = Color.White
        )

    }
}
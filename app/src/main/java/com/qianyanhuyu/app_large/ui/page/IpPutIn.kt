package com.qianyanhuyu.app_large.ui.page


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.ipPutInBg
import com.qianyanhuyu.app_large.constants.AppConfig.ipPutInBorder
import com.qianyanhuyu.app_large.constants.AppConfig.ipPutInCenterLine
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.page.common.LogoText
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.IpPutInViewEvent
import com.qianyanhuyu.app_large.viewmodel.IpPutInViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/25 9:19
 * @Description : Ip投放
 */
@Composable
fun IpPutInScreen(
    viewModel: IpPutInViewModel = hiltViewModel(),
    snackHostState: SnackbarHostState? = null,
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        // viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is IpPutInViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route)
            }
            else if (it is IpPutInViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackHostState?.showSnackbar(message = it.message)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        IpPutInContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * IpPutIn页面内容
 */
@Composable
private fun IpPutInContent(
    modifier: Modifier
) {
    // 主体内容
    Box(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 15.cdp,
                    end = 30.cdp,
                    start = 30.cdp,
                    bottom = 25.cdp
                )
        ) {
            val(
                bgView,
                centerLineView,
                leftTopView,
                leftContentView,
                rightTrips1,
                rightTrips2,
                rightTrips3,
                rightQrCodeView,
                tripsButtonView,
            ) = createRefs()

            val bgRadius = 24.cdp

            val centerGuideLine = createGuidelineFromStart(0.47f)

            // 背景Box
            Box(
                modifier = Modifier
                    .constrainAs(bgView) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .border(
                        BorderStroke(2.cdp, ipPutInBorder),
                        RoundedCornerShape(bgRadius)
                    )
                    .clip(RoundedCornerShape(bgRadius))
                    .background(ipPutInBg)
            )

            Box(
                modifier = Modifier
                    .constrainAs(centerLineView) {
                        linkTo(top = bgView.top, bottom = bgView.bottom)
                        linkTo(start = centerGuideLine, end = centerGuideLine)
                    }
                    .background(
                        ipPutInCenterLine
                    )
                    .width(3.cdp)
                    .fillMaxHeight()
            )

            CommonComposeImage(
                resId = R.drawable.img_ip_put_left_top,
                modifier = Modifier
                    .constrainAs(leftTopView) {
                        start.linkTo(parent.start)
                        end.linkTo(centerGuideLine)
                        top.linkTo(parent.top)
                        bottom.linkTo(leftContentView.top)

                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    }
                    .width(620.cdp)
                    .height(150.cdp)
            )

            CommonComposeImage(
                resId = R.drawable.img_ip_put_left,
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        start.linkTo(parent.start)
                        end.linkTo(centerGuideLine)
                        top.linkTo(leftTopView.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(652.cdp)
                    .height(567.cdp)
            )

            LogoText(
                iconDrawable = R.drawable.ic_blank,
                text = "添加投放素材（投放展示的素材）",
                modifier = Modifier
                    .constrainAs(rightTrips1) {
                        start.linkTo(centerGuideLine)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)

                        width = Dimension.preferredWrapContent
                    }
                    .fillMaxWidth()
                    .padding(
                        top = 60.cdp,
                        start = 200.cdp
                    )
            )

            LogoText(
                iconDrawable = R.drawable.ic_blank,
                text = "投放类型：视频/图文/游戏",
                modifier = Modifier
                    .constrainAs(rightTrips2) {
                        start.linkTo(centerGuideLine)
                        end.linkTo(parent.end)
                        top.linkTo(rightTrips1.bottom)

                        width = Dimension.preferredWrapContent
                    }
                    .fillMaxWidth()
                    .padding(
                        top = 30.cdp,
                        start = 200.cdp
                    )
            )

            LogoText(
                iconDrawable = R.drawable.ic_blank,
                text = "请扫下方的二维码进入小程序填写投放信息",
                modifier = Modifier
                    .constrainAs(rightTrips3) {
                        start.linkTo(centerGuideLine)
                        end.linkTo(parent.end)
                        top.linkTo(rightTrips2.bottom)

                        width = Dimension.preferredWrapContent
                    }
                    .fillMaxWidth()
                    .padding(
                        top = 30.cdp,
                        start = 200.cdp,
                        bottom = 55.cdp
                    )
            )

            CommonNetworkImage(
                url = "https://img.js.design/assets/img/640db47e7cc708fcf0d12938.png",
                modifier = Modifier
                    .constrainAs(rightQrCodeView) {
                        top.linkTo(rightTrips3.bottom)
                        start.linkTo(centerGuideLine)
                        end.linkTo(parent.end)
                    }
                    .width(435.cdp)
                    .aspectRatio(1f)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(tripsButtonView) {
                        top.linkTo(rightQrCodeView.bottom)
                        start.linkTo(centerGuideLine)
                        end.linkTo(parent.end)
                    }
                    .padding(
                        top = 55.cdp,
                        bottom = 30.cdp
                    )
            ) {
                CommonComposeImage(
                    resId = R.drawable.ip_trips_button_bg,
                    modifier = Modifier
                        .width(392.cdp)
                        .height(59.cdp)
                )
                Text(
                    "前往小程序添加信息投放",
                    fontSize = 30.csp,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}


package com.qianyanhuyu.app_large.ui.page


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.page.common.CustomButton
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.constants.AppConfig.CustomBlue
import com.qianyanhuyu.app_large.constants.AppConfig.CustomBlue9
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewEvent
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 9:19
 * @Description : 迁眼送
 */

private val imageRadius = 15.cdp

@Composable
fun QianYanGiveScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: QianYanGiveViewModel = hiltViewModel()
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        // viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is QianYanGiveViewEvent.NavTo) {

            }
            else if (it is QianYanGiveViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        QianYanGiveContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * QianYanGive页面内容
 */
@Composable
fun QianYanGiveContent(
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
                tipsView,
                centerContentView,
                leftContentView,
                rightContentView
            ) = createRefs()

            // 顶部提示栏布局
            CustomTopTrips(
                text = "温馨提示: 请选择您需要的菜单功能",
                modifier = Modifier
                    .constrainAs(tipsView) {
                        top.linkTo(parent.top)
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.preferredWrapContent
                    }
                    .padding(
                        bottom = 28.cdp,
                        start = 116.cdp,
                        end = 116.cdp
                    )
            )

            // 左边布局
            FillHeightContent(
                src = "https://img.js.design/assets/img/6259ca77ba0a72d317f8dff4.png",
                title = "免费送餐",
                subTitle = "免费领取酒店早餐",
                buttonText = "去领取",
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        start.linkTo(parent.start)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.32f)
            )

            // 右边布局
            FillHeightContent(
                src = "https://img.js.design/assets/img/617fd7537e06ae29ef55e3e8.png",
                title = "免费送餐",
                subTitle = "免费领取酒店早餐",
                buttonText = "去领取",
                modifier = Modifier
                    .constrainAs(rightContentView) {
                        end.linkTo(parent.end)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.32f)
            )

            // 中间布局
            FillHeightContent(
                src = "https://img.js.design/assets/img/6184ce18d97511650cd34cfd.png",
                buttonText = "去领取",
                title = "免费门票",
                subTitle = "赠送附近酒店门票",
                modifier = Modifier
                    .constrainAs(centerContentView) {
                        start.linkTo(leftContentView.end)
                        end.linkTo(rightContentView.start)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.32f)
            )

        }
    }
}

@Composable
fun FillHeightContent(
    src: String,
    title: String,
    subTitle: String,
    buttonText: String,
    buttonColor: Color = CustomBlue9,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(imageRadius))
    ) {

        val (
            titleView,
            subTitleView,
            lineDotView,
            contentBgView,
            buttonView
        ) = createRefs()

        CommonNetworkImage(
            url = src,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )


        Box(
            modifier = Modifier
                .constrainAs(contentBgView) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
                .fillMaxHeight(0.25f)
                .background(Color.White)
        )

        // Button
        CustomButton(
            text = buttonText,
            containerColor = buttonColor,
            shape = Shapes.small,
            modifier = Modifier
                .constrainAs(buttonView) {
                    end.linkTo(parent.end)
                    top.linkTo(contentBgView.top)
                    bottom.linkTo(contentBgView.bottom)
                }
                .padding(
                    end = 35.cdp
                )
        ) {

        }

        createVerticalChain(
            titleView,
            lineDotView,
            subTitleView,
            chainStyle = ChainStyle.Packed(0.96f)
        )

        // title
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 50.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 1.csp,
            color = CustomBlue9,
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(parent.start)
                    top.linkTo(contentBgView.top)
                    end.linkTo(buttonView.start)

                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = 40.cdp,
                    top = 35.cdp,
                )
        )

        // line_dot
        CommonComposeImage(
            resId = R.drawable.ic_line_dot,
            modifier = Modifier
                .constrainAs(lineDotView) {
                    start.linkTo(parent.start)
                    top.linkTo(titleView.bottom)
                }
                .padding(
                    start = 40.cdp,
                    top = 30.cdp,
                    bottom = 30.cdp
                )
                .width(79.cdp)
                .height(12.cdp)
        )

        // subtitle
        Text(
            text = subTitle,
            fontSize = 30.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 1.csp,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(subTitleView) {
                    start.linkTo(parent.start)
                    top.linkTo(lineDotView.bottom)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(buttonView.start)

                    width = Dimension.fillToConstraints

                }
                .padding(
                    start = 40.cdp,
                )
        )
    }
}
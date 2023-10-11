package com.qianyanhuyu.app_large.ui.page


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.ui.page.common.CustomButton
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.constants.AppConfig.CustomBlue
import com.qianyanhuyu.app_large.constants.AppConfig.CustomGreen9
import com.qianyanhuyu.app_large.constants.AppConfig.CustomRed
import com.qianyanhuyu.app_large.constants.AppConfig.originToBlueHorizontal
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToBlack
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToBlackHorizontal
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToGreenHorizontal
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.BaseMsgDialog
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewEvent
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 14:00
 * @Description : 迁眼互娱
 */

private val imageRadius = 15.cdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QianYanPlayScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: QianYanPlayViewModel = hiltViewModel()
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        // viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is QianYanPlayViewEvent.NavTo) {

            }
            else if (it is QianYanPlayViewEvent.ShowMessage) {
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
        QianYanPlayContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * QianYanPlay页面内容
 */
@Composable
fun QianYanPlayContent(
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
            FillHeightImageContent(
                src = "https://img.js.design/assets/img/643d15f1097d676c83ee3656.png",
                iconSrc = "https://img.js.design/assets/img/64747616d18b309f6e7d8594.png#d1e4aa5e2fccfeb1b9eba374eabad772",
                buttonText = "腾讯视频",
                isTripsVisible = true,
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        start.linkTo(parent.start)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.25f)
            )

            // 右边布局
            FillHeightImageContent(
                src = "https://img.js.design/assets/img/64c4c8683835e991fbfa7bd8.png#c14c965f63603d513ff4723e904bc1ec",
                iconSrc = "https://img.js.design/assets/img/6497edd31e215bbdd2c52c44.png#cf6c4a4e8db29bee13fc4ce69a3bafb4",
                buttonText = "优酷",
                buttonColor = CustomBlue,
                modifier = Modifier
                    .constrainAs(rightContentView) {
                        end.linkTo(parent.end)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.25f)
            )

            CenterContent(
                src = "https://img.js.design/assets/img/643d15f1097d676c83ee361a.png",
                bottomLeftSrc = "https://img.js.design/assets/img/64c4cad7a2cdceb1b17f754a.png#433538b4f062f82e87f2cb6eac7fdba5",
                bottomRightSrc = "https://img.js.design/assets/img/64c4cad7a2cdceb1b17f754a.png#433538b4f062f82e87f2cb6eac7fdba5",
                modifier = Modifier
                    .constrainAs(centerContentView) {
                        start.linkTo(leftContentView.end)
                        end.linkTo(rightContentView.start)
                        top.linkTo(tipsView.bottom)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        horizontal = 30.cdp
                    )
            )

        }
    }
}

@Composable
fun CenterContent(
    src: String,
    bottomLeftSrc: String,
    bottomRightSrc: String,
    modifier: Modifier
){
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            topView,
            topButtonView,
            bottomLeftView,
            bottomRightView,
            contentTopBgView
        ) = createRefs()

        // 底部中线
        val guideLine5f = createGuidelineFromTop(0.5f)

        // 底部中线
        val guideLine5fV = createGuidelineFromStart(0.5f)

        val bgColorsBrush = remember {
            whiteToBlackHorizontal
        }

        CommonNetworkImage(
            url = src,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(topView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(guideLine5f)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(
                    bottom = 30.cdp
                )
                .clip(RoundedCornerShape(imageRadius))
        )

        Box(
            modifier = Modifier
                .constrainAs(contentTopBgView) {
                    top.linkTo(topView.top)
                    bottom.linkTo(topView.bottom)
                    end.linkTo(topView.end)

                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth(0.5f)
                .background(whiteToGreenHorizontal)
        )

        // Button
        CustomButton(
            text = "爱奇艺",
            containerColor = CustomGreen9,
            shape = Shapes.extraSmall,
            isShowIcon = true,
            modifier = Modifier
                .constrainAs(topButtonView) {
                    end.linkTo(parent.end)
                    top.linkTo(contentTopBgView.top)
                    bottom.linkTo(contentTopBgView.bottom)
                }
                .padding(
                    end = 50.cdp
                )
        ) {

        }

        CommonComposeImage(
            R.drawable.test_xiaohongshu,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(bottomLeftView) {
                    start.linkTo(parent.start)
                    top.linkTo(guideLine5f)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(guideLine5fV)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(
                    end = 15.cdp
                )
                .clip(RoundedCornerShape(imageRadius))
        )

        CommonComposeImage(
            R.drawable.test_douyin,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(bottomRightView) {
                    start.linkTo(guideLine5fV)
                    end.linkTo(parent.end)
                    top.linkTo(guideLine5f)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = 15.cdp
                )
                .clip(RoundedCornerShape(imageRadius))
        )
    }
}

@Composable
fun FillHeightImageContent(
    src: String,
    iconSrc: String,
    buttonText: String,
    buttonColor: Color = CustomBlue,
    isTripsVisible: Boolean = false,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(imageRadius))
    ) {

        val (
            imageView,
            iconView,
            tripsView,
            contentBgView,
            buttonView
        ) = createRefs()

        CommonNetworkImage(
            url = src,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        // Icon
        CommonNetworkImage(
            url = iconSrc,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(iconView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clip(RoundedCornerShape(imageRadius))
                .padding(
                    top = 20.cdp,
                    start = 16.cdp
                )
                .size(60.cdp)
        )

        // Tag
        AnimatedVisibility(
            visible = isTripsVisible,
            modifier = Modifier
                .constrainAs(tripsView) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(
                    top = 30.cdp,
                    end = 20.cdp
                )
        ) {
            TextBackground(
                text = "正在热播",
                textBackground = CustomRed,
                modifier = Modifier
            )
        }

        val bgColorsBrush = remember {
            whiteToBlack
        }

        Box(
            modifier = Modifier
                .constrainAs(contentBgView) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
                .fillMaxHeight(0.5f)
                .background(
                    bgColorsBrush
                )
        )

        // Button
        CustomButton(
            text = buttonText,
            containerColor = buttonColor,
            shape = Shapes.extraSmall,
            isShowIcon = true,
            modifier = Modifier
                .constrainAs(buttonView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(
                    bottom = 25.cdp
                )
        )
    }
}
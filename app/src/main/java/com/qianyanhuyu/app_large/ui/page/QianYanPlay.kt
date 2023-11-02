package com.qianyanhuyu.app_large.ui.page


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
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
import com.qianyanhuyu.app_large.ui.page.common.CustomButton
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.constants.AppConfig.CustomBlue
import com.qianyanhuyu.app_large.constants.AppConfig.CustomGreen9
import com.qianyanhuyu.app_large.constants.AppConfig.CustomRed
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToBlack
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToGreenHorizontal
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.OtherAppUtil
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewAction
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewEvent
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewModel
import com.qianyanhuyu.app_large.viewmodel.QianYanPlayViewState
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 14:00
 * @Description : 迁眼互娱
 */

private val imageRadius = 15.cdp

@Composable
fun QianYanPlayScreen(
    viewModel: QianYanPlayViewModel = hiltViewModel(),
    snackHostState: SnackbarHostState? = null,
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(QianYanPlayViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is QianYanPlayViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route)
            }
            else if (it is QianYanPlayViewEvent.ShowMessage) {
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
        QianYanPlayContent(
            viewState = viewModel.viewStates,
            playButtonOnClick = {
                viewModel.dispatch(QianYanPlayViewAction.OpenOtherApp(it))
            },
            modifier = Modifier
                .fillMaxSize()
        )

        if(viewModel.viewStates.isLoading)
            LoadingComponent(
                isScreen = true
            )
    }
}

/**
 * QianYanPlay页面内容
 *
 * https://img.js.design/assets/img/643d15f1097d676c83ee3656.png
 * https://img.js.design/assets/img/643d15f1097d676c83ee361a.png,
 * https://img.js.design/assets/img/64c4cad7a2cdceb1b17f754a.png#433538b4f062f82e87f2cb6eac7fdba5,
 * https://img.js.design/assets/img/64c4cad7a2cdceb1b17f754a.png#433538b4f062f82e87f2cb6eac7fdba5,
 * https://img.js.design/assets/img/64c4c8683835e991fbfa7bd8.png#c14c965f63603d513ff4723e904bc1ec
 * */
@Composable
fun QianYanPlayContent(
    viewState: QianYanPlayViewState,
    playButtonOnClick: (OtherAppUtil.OtherPackage) -> Unit,
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
                src = viewState.data.getOrNull(0)?.image ?:"",
                iconSrc = "https://img.js.design/assets/img/64747616d18b309f6e7d8594.png#d1e4aa5e2fccfeb1b9eba374eabad772",
                buttonText = "腾讯视频",
                isTripsVisible = true,
                onClick = { playButtonOnClick.invoke(OtherAppUtil.OtherPackage.TENCENT) },
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
                src = viewState.data.getOrNull(4)?.image ?: "",
                iconSrc = "https://img.js.design/assets/img/6497edd31e215bbdd2c52c44.png#cf6c4a4e8db29bee13fc4ce69a3bafb4",
                buttonText = "优酷",
                buttonColor = CustomBlue,
                onClick = { playButtonOnClick.invoke(OtherAppUtil.OtherPackage.YOU_KU) },
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
                src = viewState.data.getOrNull(1)?.image ?: "",
                bottomLeftSrc = viewState.data.getOrNull(2)?.image ?:"",
                bottomRightSrc = viewState.data.getOrNull(3)?.image ?: "",
                playButtonOnClick = playButtonOnClick,
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
private fun CenterContent(
    src: String,
    bottomLeftSrc: String,
    bottomRightSrc: String,
    playButtonOnClick: (OtherAppUtil.OtherPackage) -> Unit,
    modifier: Modifier
){
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            topView,
            topIconView,
            bottomLeftIconView,
            bottomRightIconView,
            topButtonView,
            bottomLeftView,
            bottomRightView,
            contentTopBgView
        ) = createRefs()

        // 底部中线
        val guideLine5f = createGuidelineFromTop(0.5f)

        // 底部中线
        val guideLine5fV = createGuidelineFromStart(0.5f)

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

        CommonNetworkImage(
            url = "https://img.js.design/assets/img/636cec49e8908799790b619f.png#606bb4d7123c11c9428868094e5f391b",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(topIconView) {
                    top.linkTo(topView.top)
                    start.linkTo(topView.start)
                }
                .clip(RoundedCornerShape(imageRadius))
                .padding(
                    start = 16.cdp
                )
                .width(94.cdp)
                .height(71.cdp)
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
                }
                .padding(
                    end = 25.cdp,
                    top = 25.cdp
                )
        ) {
            playButtonOnClick.invoke(OtherAppUtil.OtherPackage.AI_QI_YI)
        }

        CommonNetworkImage(
            url = bottomLeftSrc,
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

        CommonNetworkImage(
            url = "https://img.js.design/assets/img/6543587e7ad15f0a2507da2f.png#f246cb31add23f2d5e3593aa1fbad8de",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(bottomLeftIconView) {
                    top.linkTo(bottomLeftView.top)
                    end.linkTo(bottomLeftView.end, margin = 15.cdp)
                }
                .width(221.cdp)
                .height(122.cdp)
                .clip(RoundedCornerShape(imageRadius))
                .onClick {
                    playButtonOnClick.invoke(OtherAppUtil.OtherPackage.SMALL_HONG_SHU)
                }
        )

        CommonNetworkImage(
            url = bottomRightSrc,
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

        CommonNetworkImage(
            url = "https://img.js.design/assets/img/654357180132d638d3e8de2c.png#15d99cfb2a362e8fe599a39eac787adc",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(bottomRightIconView) {
                    top.linkTo(bottomRightView.top)
                    end.linkTo(bottomRightView.end, margin = 25.cdp)
                }
                .width(219.cdp)
                .height(157.cdp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(imageRadius))
                .onClick {
                    playButtonOnClick.invoke(OtherAppUtil.OtherPackage.DOU_YIN)
                }
        )
    }
}

@Composable
private fun FillHeightImageContent(
    src: String,
    iconSrc: String,
    buttonText: String,
    buttonColor: Color = CustomBlue,
    isTripsVisible: Boolean = false,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(imageRadius))
    ) {

        val (
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
                ),
            onClick = onClick
        )
    }
}
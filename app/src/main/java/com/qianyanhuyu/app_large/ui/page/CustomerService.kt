package com.qianyanhuyu.app_large.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.constants.AppConfig.CustomGreen
import com.qianyanhuyu.app_large.constants.AppConfig.CustomYellowish
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToBlackVertical7f
import com.qianyanhuyu.app_large.constants.AppConfig.whiteToGreenHorizontal
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.theme.youSheFamily
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.CustomerServiceViewAction
import com.qianyanhuyu.app_large.viewmodel.CustomerServiceViewEvent
import com.qianyanhuyu.app_large.viewmodel.CustomerServiceViewModel
import com.qianyanhuyu.app_large.viewmodel.CustomerServiceViewState
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/15 9:19
 * @Description : 客房服务
 */
@Composable
fun CustomerServiceScreen(
    snackHostState: SnackbarHostState? = null,
    viewModel: CustomerServiceViewModel = hiltViewModel()
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(CustomerServiceViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is CustomerServiceViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route)
            }
            else if (it is CustomerServiceViewEvent.ShowMessage) {
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
        CustomerServiceContent(
            viewState = viewModel.viewStates,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * CustomerService页面内容
 *
 * 测试数据地址:
 * https://img.js.design/assets/img/64c4ac847fb47e5020773a74.png#9c0460d34524da3540cda829c709b098
 * https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png
 * https://img.js.design/assets/img/64b4d72aa669203171642f06.png
 *
 */
@Composable
fun CustomerServiceContent(
    viewState: CustomerServiceViewState,
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
                    start = 30.cdp
                )
        ) {
            val(
                tipsView,
                leftContentView,
                centerContentView,
                rightContentView
            ) = createRefs()

            // 底部中线
            val guideLine9f = createGuidelineFromTop(0.9f)

            val contentMaxRadius = 24.cdp
            val contentRadius = 15.cdp

            // 顶部提示栏布局
            CustomTopTrips(
                text = "温馨提示:请选择您需要的菜单功能",
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

            // 左边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        linkTo(
                            top = tipsView.bottom,
                            bottom = guideLine9f
                        )
                        start.linkTo(parent.start)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.33f)
            ) {

                val imageContentView = createRef()

                CommonNetworkImage(
                    url = viewState.data.getOrNull(0)?.image?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(contentMaxRadius))
                        .fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(contentMaxRadius))
                        .fillMaxSize()
                        .background(whiteToBlackVertical7f)
                )

                ImageContentView(
                    title = "呼叫前台",
                    subTitle = "专业服务关怀亲切",
                    buttonText = "立即呼叫",
                    isShowTextBackgroundView = true,
                    titleFontWeight = FontWeight.Bold,
                    paddingHorizontal = 60.cdp,
                    buttonColor = AppConfig.CustomButtonBrushPurple,
                    modifier = Modifier
                        .constrainAs(imageContentView) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                        }
                )

            }

            // 中间图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(centerContentView) {
                        linkTo(
                            start = leftContentView.end,
                            end = rightContentView.start
                        )
                        linkTo(
                            top = tipsView.bottom,
                            bottom = guideLine9f
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 30.cdp,
                        end = 30.cdp
                    )
            ) {
                val (
                    topView,
                    topBgView,
                    topContentView,
                    bottomView,
                    bottomBgView,
                    bottomContentView
                ) = createRefs()

                val lineLeftContentH = createGuidelineFromTop(0.5f)

                CommonNetworkImage(
                    url = viewState.data.getOrNull(1)?.image?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .constrainAs(topView) {
                            top.linkTo(parent.top)
                            bottom.linkTo(lineLeftContentH, margin = 15.cdp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )

                Box(
                    modifier = Modifier
                        .constrainAs(topBgView) {
                            top.linkTo(parent.top)
                            bottom.linkTo(lineLeftContentH)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .padding(
                            bottom = 15.cdp
                        )
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )

                ImageContentView(
                    title = "酒店商超",
                    subTitle = "商城配送急速送达",
                    buttonText = "去购买",
                    buttonColor = AppConfig.CustomButtonBrushOrigin,
                    paddingHorizontal = 60.cdp,
                    titleFontFamily = youSheFamily,
                    modifier = Modifier
                        .constrainAs(topContentView) {
                            top.linkTo(topView.top)
                            bottom.linkTo(topView.bottom)
                            start.linkTo(topView.start)

                            width = Dimension.fillToConstraints
                            height = Dimension.preferredWrapContent
                        }
                )

                CommonNetworkImage(
                    url = viewState.data.getOrNull(2)?.image?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .constrainAs(bottomView) {
                            top.linkTo(lineLeftContentH)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .padding(
                            top = 15.cdp
                        )
                        .clip(RoundedCornerShape(contentMaxRadius))
                        .fillMaxHeight()
                )

                Box(
                    modifier = Modifier
                        .constrainAs(bottomBgView) {
                            top.linkTo(lineLeftContentH)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                        .padding(
                            top = 15.cdp
                        )
                        .background(whiteToGreenHorizontal)
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )

                ImageContentView(
                    title = "干洗服务",
                    subTitle = "全家健康快乐生活",
                    buttonText = "查看服务",
                    buttonColor = AppConfig.CustomButtonBrushBlack,
                    paddingVertical = 15.cdp,
                    paddingHorizontal = 30.cdp,
                    titleFontFamily = youSheFamily,
                    modifier = Modifier
                        .constrainAs(bottomContentView) {
                            top.linkTo(bottomView.top)
                            bottom.linkTo(bottomView.bottom)
                            end.linkTo(bottomView.end)

                            width = Dimension.fillToConstraints
                            height = Dimension.preferredWrapContent
                        }
                ) {
                    AppNavController.instance.navigate(Route.DRY_CLEAN)
                }
            }

            // 右边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(rightContentView) {
                        linkTo(
                            top = tipsView.bottom,
                            bottom = guideLine9f
                        )
                        end.linkTo(parent.end)

                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(0.33f)
            ) {

                val imageContentView = createRef()

                CommonNetworkImage(
                    url = viewState.data.getOrNull(3)?.image?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(contentMaxRadius))
                        .fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(contentMaxRadius))
                        .fillMaxSize()
                        .background(whiteToBlackVertical7f)
                )

                ImageContentView(
                    title = "餐饮配送",
                    subTitle = "商城配送极速送达",
                    buttonText = "去点好吃的",
                    isShowTextBackgroundView = false,
                    paddingHorizontal = 60.cdp,
                    buttonColor = AppConfig.CustomButtonBrushOrigin,
                    titleFontFamily = youSheFamily,
                    modifier = Modifier
                        .constrainAs(imageContentView) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                            width = Dimension.fillToConstraints
                        }
                )

            }
        }
    }
}

@Composable
private fun ImageContentView (
    title: String,
    subTitle: String,
    buttonText: String,
    buttonColor: Brush? = null,
    isShowTextBackgroundView: Boolean = false,
    paddingHorizontal: Dp = 0.cdp,
    paddingVertical: Dp = 35.cdp,
    titleFontFamily: FontFamily? = null,
    titleFontWeight: FontWeight? = null,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .padding(
                horizontal = paddingHorizontal,
                vertical = paddingVertical
            )
    ) {

        val(
            textView1,
            textView11,
            textView2,
            iconView3,
            textView3,
            buttonView4
        ) = createRefs()

        if(isShowTextBackgroundView) {
            createHorizontalChain(
                textView1, textView11,
                chainStyle = ChainStyle.Packed(0f)
            )

            TextBackground(
                text = "服务标准",
                shapes = Shapes.large,
                modifier = Modifier
                    .constrainAs(textView1) {
                        top.linkTo(parent.top)
                    }
            )

            TextBackground(
                text = "亲切关怀",
                textBackground = CustomYellowish,
                shapes = Shapes.large,
                modifier = Modifier
                    .constrainAs(textView11) {
                        top.linkTo(parent.top)
                    }
                    .padding(
                        start = 60.cdp
                    )
            )
        }

        Text(
            title,
            fontSize = 79.csp,
            fontWeight = titleFontWeight,
            textAlign = TextAlign.Center,
            letterSpacing = 4.csp,
            fontFamily = titleFontFamily,
            color = Color.White,
            modifier = Modifier
                .constrainAs(textView2) {
                    if (isShowTextBackgroundView) top.linkTo(textView1.bottom) else top.linkTo(
                        parent.top
                    )
                    start.linkTo(parent.start)
                }
                .padding(
                    top = if(isShowTextBackgroundView) { 35.cdp } else { 0.cdp },
                )
        )

        CommonIcon(
            R.drawable.ic_green_dot,
            tint = CustomGreen,
            modifier = Modifier
                .constrainAs(iconView3) {
                    start.linkTo(parent.start)
                    top.linkTo(textView3.top)
                    bottom.linkTo(textView3.bottom)
                }
                .width(18.cdp)
                .aspectRatio(1f)
        )

        Text(
            subTitle,
            fontSize = 30.csp,
            letterSpacing = 4.csp,
            color = Color.White,
            modifier = Modifier
                .constrainAs(textView3) {
                    top.linkTo(textView2.bottom)
                    start.linkTo(iconView3.end)
                }
                .padding(
                    start = 20.cdp,
                )
        )

        TextBackground(
            text = buttonText,
            shapes = Shapes.small,
            fontSize = 30.csp,
            textHorizontalPadding = 20.cdp,
            textVerticalPadding = 10.cdp,
            textBackgroundBrush = buttonColor,
            onClick = onClick,
            modifier = Modifier
                .constrainAs(buttonView4) {
                    start.linkTo(parent.start)
                    top.linkTo(textView3.bottom, margin = 35.cdp)
                }
                .zIndex(9999f)
        )
    }
}
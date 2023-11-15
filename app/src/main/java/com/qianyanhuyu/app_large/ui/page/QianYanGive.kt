package com.qianyanhuyu.app_large.ui.page


import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.ui.page.common.CustomButton
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.constants.AppConfig.CustomBlue9
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.theme.youSheFamily
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.ui.widgets.drawColoredShadow
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewAction
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewEvent
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewModel
import com.qianyanhuyu.app_large.viewmodel.QianYanGiveViewState
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 9:19
 * @Description : 迁眼送
 */

private val imageRadius = 15.cdp

@Composable
fun QianYanGiveScreen(
    viewModel: QianYanGiveViewModel = hiltViewModel(),
    snackHostState: SnackbarHostState? = null,
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(QianYanGiveViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is QianYanGiveViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route)
            }
            else if (it is QianYanGiveViewEvent.ShowMessage) {
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
        QianYanGiveContent(
            viewState = viewModel.viewStates,
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
 * QianYanGive页面内容
 * https://img.js.design/assets/img/6259ca77ba0a72d317f8dff4.png
 * https://img.js.design/assets/img/617fd7537e06ae29ef55e3e8.png
 * https://img.js.design/assets/img/6184ce18d97511650cd34cfd.png
 *
 */
@Composable
fun QianYanGiveContent(
    viewState: QianYanGiveViewState,
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
                text = "温馨提示：通过玩游戏获得生活积分可兑换提现",
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
                src = viewState.data.getOrNull(0)?.image ?: "",
                title = "免费送餐",
                buttonText = "去领取",
                lineDotColor = AppConfig.CustomGreen,
                tagResource = R.drawable.ic_play_tag_1,
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
                src = viewState.data.getOrNull(2)?.image ?: "",
                title = "免费门票",
                lineDotColor = AppConfig.CustomBlue,
                buttonText = "去领取",
                tagResource = R.drawable.ic_play_tag_3,
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
                src = viewState.data.getOrNull(1)?.image ?: "",
                buttonText = "去领取",
                title = "免费住房",
                lineDotColor = AppConfig.CustomYellowish,
                tagResource = R.drawable.ic_play_tag_2,
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
    buttonText: String,
    lineDotColor: Color = Color.Transparent,
    @DrawableRes tagResource: Int = R.drawable.ic_play_tag_1,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .clip(RoundedCornerShape(imageRadius))
    ) {

        val (
            titleView,
            lineDotView,
            buttonView,
            tagView,
        ) = createRefs()

        CommonNetworkImage(
            url = src,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )

        CommonComposeImage(
            resId = tagResource,
            modifier = Modifier
                .constrainAs(tagView) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .graphicsLayer {
                    this.translationY = -50.cdp.toPx
                }
        )

        TextBackground(
            text = buttonText,
            shapes = Shapes.extraLarge,
            fontSize = 50.csp,
            textVerticalPadding = 15.cdp,
            textBackgroundBrush = Brush.horizontalGradient(
                listOf(
                    Color(255, 94, 59).copy(alpha = 0.3f),
                    Color(255, 94, 59).copy(alpha = 0f),
                    Color(0, 0, 0).copy(alpha = 0f)
                )
            ),
            modifier = Modifier
                .constrainAs(buttonView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        createVerticalChain(
            titleView,
            lineDotView,
            chainStyle = ChainStyle.Packed(0f)
        )

        // title
        CommonText(
            text = title,
            fontSize = 79.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 9.csp,
            color = Color.White,
            style = TextStyle(
                fontFamily = youSheFamily
            ),
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(
                    top = 50.cdp,
                )
        )

        // line_dot
        CommonIcon(
            resId = R.drawable.ic_line_dot,
            tint = lineDotColor,
            modifier = Modifier
                .constrainAs(lineDotView) {
                    start.linkTo(titleView.start)
                    top.linkTo(titleView.bottom)
                }
                .padding(
                    top = 30.cdp,
                    bottom = 30.cdp
                )
                .width(79.cdp)
                .height(12.cdp)
        )
    }
}
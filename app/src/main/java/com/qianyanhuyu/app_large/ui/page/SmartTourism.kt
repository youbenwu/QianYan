package com.qianyanhuyu.app_large.ui.page


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.CustomTopTrips
import com.qianyanhuyu.app_large.ui.page.common.SimpleLazyGrid
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.SmartTourismViewEvent
import com.qianyanhuyu.app_large.viewmodel.SmartTourismViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/23 9:19
 * @Description : 智慧旅游
 */

private val imageRadius = 15.cdp

@Composable
fun SmartTourismScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: SmartTourismViewModel = hiltViewModel()
) {

    val coroutineState = rememberCoroutineScope()

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        // viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is SmartTourismViewEvent.NavTo) {

            }
            else if (it is SmartTourismViewEvent.ShowMessage) {
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
        SmartTourismContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * SmartTourism页面内容
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SmartTourismContent(
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
                    bottom = 25.cdp
                )
        ) {
            val(
                tipsView,
                contentView,
                pagerIndicatorView,
            ) = createRefs()

            // 顶部提示栏布局
            CustomTopTrips(
                text = "最新通知! 即日起恢复全国旅行社及在线旅游企业经营外国人入境团队旅游",
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

            val pagerState = rememberPagerState(
                initialPage = 0,
            )

            HorizontalPager(
                count = 4,
                state = pagerState,
                modifier = Modifier
                    .constrainAs(contentView) {
                        top.linkTo(tipsView.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.preferredWrapContent
                    }
            ) { _ ->
                ContentList(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(pagerIndicatorView) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.cdp
                    )
            ) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = Color.Black,
                    inactiveColor = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                )
            }

        }
    }
}

// 列表内容
@Composable
fun ContentList(
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 30.cdp,
                end = 30.cdp
            )
    ) {
        // 竖中线
        val guideLine4fH = createGuidelineFromStart(0.4f)

        val (
            adView,
            bannerView,
            bannerBgView,
            rightListView,
            titleView,
            tagView,
            subTitleView
        ) = createRefs()

        CommonNetworkImage(
            url = "https://img.js.design/assets/img/62c3a3214835c75a14d9f537.png",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(bannerView) {
                    start.linkTo(parent.start)
                    end.linkTo(guideLine4fH)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(
                    end = 30.cdp
                )
                .clip(RoundedCornerShape(imageRadius))
        )

        Box(
            modifier = Modifier
                .constrainAs(bannerBgView) {
                    bottom.linkTo(bannerView.bottom)
                    start.linkTo(bannerView.start)
                    end.linkTo(bannerView.end)
                    top.linkTo(bannerView.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxHeight()
                .background(
                    AppConfig.blackToWhite
                )
        )

        ContentListLeftAdView(
            modifier = Modifier
                .constrainAs(adView) {
                    top.linkTo(bannerView.top)
                    bottom.linkTo(titleView.top)
                    start.linkTo(bannerView.start)
                    end.linkTo(bannerView.end)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(
                    end = 30.cdp
                )
        )

        createVerticalChain(
            titleView,
            tagView,
            subTitleView,
            chainStyle = ChainStyle.Packed(0.96f)
        )

        CommonText(
            text = "打卡经典地标，厦门海滨风情4日游",
            fontSize = 40.csp,
            textAlign = TextAlign.Left,
            maxLines = 2,
            letterSpacing = 1.csp,
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(guideLine4fH)

                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = 40.cdp,
                    top = 35.cdp,
                    end = 67.cdp
                )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(30.cdp),
            modifier = Modifier
                .constrainAs(tagView) {
                    start.linkTo(parent.start)
                    top.linkTo(titleView.top)
                    end.linkTo(guideLine4fH)

                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = 40.cdp,
                    end = 67.cdp,
                    top = 30.cdp,
                    bottom = 30.cdp
                )
        ) {
            listOf(
                "温泉",
                "经典"
            ).forEach {
                    TextBackground(
                        text = it,
                        textBackground = Color.Transparent,
                        fontSize = 28.csp,
                        textHorizontalPadding = 5.cdp,
                        modifier = Modifier
                            .border(
                                1.cdp,
                                Color.White
                            )
                    )
                }
        }

        CommonText(
            text = "打卡厦门经典地标，悠闲享受海滨风情,去厦大感受人文氛围,花一天的时间慢慢逛江鼓浪屿。",
            fontSize = 20.csp,
            textAlign = TextAlign.Left,
            maxLines = 3,
            letterSpacing = 1.csp,
            color = Color.White,
            lineHeight = 25.csp,
            modifier = Modifier
                .constrainAs(subTitleView) {
                    start.linkTo(parent.start)
                    top.linkTo(tagView.bottom)
                    end.linkTo(guideLine4fH)

                    width = Dimension.fillToConstraints

                }
                .padding(
                    start = 40.cdp,
                    end = 67.cdp
                )
        )

        // 右边列表
        ContentListRight(
            modifier = Modifier
                .constrainAs(rightListView) {
                    start.linkTo(guideLine4fH)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun ContentListLeftAdView(
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.cdp, Alignment.CenterVertically),
        modifier = modifier.fillMaxSize().padding(40.cdp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.cdp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CommonLocalImage(
                resId = R.drawable.ic_image_placeholder,
                modifier = Modifier
                    .width(75.cdp)
                    .height(48.cdp)
            )

            CommonText(
                "世界那么大我想去看看",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .weight(1f)
            )

            CommonLocalImage(
                resId = R.drawable.ic_collect_true,
                modifier = Modifier
                    .width(55.cdp)
                    .aspectRatio(1f)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            CommonNetworkImage(
                url = "https://img.js.design/assets/img/64c712d13835e991fb17fa0f.png#f752b9346890182b879946913c89a6b5",
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .aspectRatio(2f)
                    .border(
                        3.cdp,
                        Color.Black
                    )
            )
        }
    }
}

/**
 * 右边列表
 */
@Composable
fun ContentListRight(
    modifier: Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        SimpleLazyGrid(
            lists = listOf(
                "https://img.js.design/assets/img/62c3a3214835c75a14d9f537.png",
                "https://img.js.design/assets/img/617a65656047a179b22639e6.png",
                "https://img.js.design/assets/img/62c3a2e71eee627d163d9641.png",
                "https://img.js.design/assets/img/62c3a2e71eee627d163d9641.png",
                "https://img.js.design/assets/img/617a65656047a179b22639e6.png",
                "https://img.js.design/assets/img/62c3a3214835c75a14d9f537.png",
            ),
            modifier = Modifier
                .fillMaxSize()
        ) { src ->
            ContentListRightItem(
                src = src,
                modifier = Modifier
                    .height(maxHeight / 2 - 15.cdp)
            )
        }
    }

}

// 右边列表Item
@Composable
fun ContentListRightItem(
    src: String,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(imageRadius))
    ) {

        val (
            imageView,
            titleView,
            subTitleView,
            bgView,
            locationIconView,
            locationTextView,
            priceView,
        ) = createRefs()

        CommonNetworkImage(
            url = src,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(imageView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        Box(
            modifier = Modifier
                .constrainAs(bgView) {
                    bottom.linkTo(imageView.bottom)
                    start.linkTo(imageView.start)
                    end.linkTo(imageView.end)

                    width = Dimension.fillToConstraints
                }
                .fillMaxHeight(0.75f)
                .background(
                    AppConfig.blackToBlack5f
                )
        )

        TextBackground(
            text = "$400",
            textBackground = AppConfig.CustomOrigin,
            textHorizontalPadding = 10.cdp,
            fontSize = 18.csp,
            lineHeight = 23.csp,
            letterSpacing = 1.csp,
            modifier = Modifier
                .constrainAs(priceView) {
                    bottom.linkTo(imageView.bottom)
                    end.linkTo(imageView.end)
                }
                .padding(
                    bottom = 20.cdp,
                    end = 15.cdp
                )
        )

        createVerticalChain(
            titleView,
            subTitleView,
            locationIconView,
            chainStyle = ChainStyle.Packed(0.96f)
        )

        // title
        CommonText(
            text = "旅游圣地",
            fontWeight = FontWeight.Bold,
            fontSize = 25.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 1.csp,
            modifier = Modifier
                .constrainAs(titleView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = 40.cdp,
                    top = 35.cdp,
                )
        )

        // subtitle
        CommonText(
            text = "饮佐茶味正宗港式餐厅(建发JFC品尚店)",
            fontSize = 18.csp,
            textAlign = TextAlign.Left,
            maxLines = 3,
            letterSpacing = 1.csp,
            lineHeight = 23.csp,
            modifier = Modifier
                .constrainAs(subTitleView) {
                    start.linkTo(parent.start)
                    top.linkTo(titleView.bottom)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints

                }
                .padding(
                    start = 40.cdp,
                    end = 67.cdp,
                    bottom = 10.cdp
                )
        )

        // line_dot
        CommonIcon(
            resId = R.drawable.ic_location_small,
            tint = Color.White,
            modifier = Modifier
                .constrainAs(locationIconView) {
                    start.linkTo(parent.start)
                    top.linkTo(titleView.bottom)
                }
                .padding(
                    start = 40.cdp,
                    top = 10.cdp,
                    bottom = 10.cdp
                )
                .size(15.cdp)

        )

        CommonText(
            text = "1.2公里",
            fontSize = 15.csp,
            textAlign = TextAlign.Left,
            letterSpacing = 1.csp,
            modifier = Modifier
                .constrainAs(locationTextView) {
                    start.linkTo(locationIconView.end)
                    top.linkTo(locationIconView.top)
                    bottom.linkTo(locationIconView.bottom)

                    width = Dimension.fillToConstraints

                }
                .padding(
                    start = 5.cdp,
                )
        )
    }
}
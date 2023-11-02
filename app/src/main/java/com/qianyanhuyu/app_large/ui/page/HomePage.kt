package com.qianyanhuyu.app_large.ui.page

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.constants.AppConfig.brush121
import com.qianyanhuyu.app_large.constants.AppConfig.brush121_192
import com.qianyanhuyu.app_large.constants.AppConfig.brush247
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.model.AdvertType
import com.qianyanhuyu.app_large.model.MultiMenuItem
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.HomePageViewAction
import com.qianyanhuyu.app_large.viewmodel.HomePageViewEvent
import com.qianyanhuyu.app_large.viewmodel.HomePageViewModel
import com.qianyanhuyu.app_large.viewmodel.HomePageViewState
import com.qianyanhuyu.app_large.viewmodel.ShopFriendsViewAction
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Timer
import java.util.TimerTask
import kotlin.math.absoluteValue

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/15 9:19
 * @Description : 主页
 */
private const val TAG = "HomePage"

val expandFbItemList: MutableList<MultiMenuItem> = mutableListOf(
    MultiMenuItem(
        index = 0,
        icon = R.drawable.ic_nav_customer_service,
        label = "客服服务",
        route = Route.CUSTOMER_SERVICE
    ),
    MultiMenuItem(
        index = 1,
        icon = R.drawable.ic_ip_put_in,
        label = "媒体投放",
        route = Route.IP_PUT_IN
    ),
    MultiMenuItem(
        index = 2,
        icon = R.drawable.ic_nav_travel,
        label = "智慧旅游",
        route = Route.SMART_TOURISM
    ),
    MultiMenuItem(
        index = 3,
        icon = R.drawable.ic_nav_qianyan_play,
        label = "迁眼互娱",
        route = Route.QIAN_YAN_PLAY
    ),
    MultiMenuItem(
        index = 4,
        icon = R.drawable.ic_nav_store,
        label = "店友乐园",
        route = Route.SHOP_FRIENDS
    ),
    MultiMenuItem(
        index = 5,
        icon = R.drawable.ic_nav_qianyan_give,
        label = "迁眼送",
        route = Route.QIAN_YAN_GIVE
    ),
)

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomePageScreen(
    viewModel: HomePageViewModel = hiltViewModel(),
    snackHostState: SnackbarHostState?,
) {
    val coroutineState = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                //根据Event执行不同生命周期的操作
                Lifecycle.Event.ON_CREATE -> {

                }
                Lifecycle.Event.ON_RESUME -> {
                    // OnResume 里面初始化数据, 返回页面的时候也会执行
                    Log.d(TAG, "OnResume")
                    viewModel.dispatch(HomePageViewAction.InitPageData)
                }
                else -> {
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is HomePageViewEvent.NavTo) {
                AppNavController.instance.navigate(it.route)
            }
            else if (it is HomePageViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackHostState?.showSnackbar(message = it.message)
                }
            }
        }
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    // 轮播
    LaunchedEffect(Unit) {
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    coroutineState.launch() {
                        pagerState.animateScrollToPage(
                            page = if(pagerState.currentPage + 1 == 3) {
                                0
                            } else {
                                pagerState.currentPage + 1
                            },
                        )
                    }
                }
            },
            10000,
            8000
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(9f)
            ) { pagePosition ->
                AnimatedVisibility(
                    visible = pagerState.currentPage == pagePosition,
                    enter = fadeIn() + scaleIn(
                        animationSpec = tween(1500, easing = LinearEasing)
                    ),
                    exit = fadeOut() + scaleOut(
                        animationSpec = tween(1500, easing = LinearEasing)
                    )
                ) {
                    HomePageContent(
                        viewState = viewModel.viewStates,
                        pagePosition = pagePosition,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset =
                                    ((pagerState.currentPage - pagePosition) + pagerState.currentPageOffset).absoluteValue

                                val transformation =
                                    lerp(
                                        start = 0.7f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                alpha = transformation
                                scaleY = transformation
                            }
                    )
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = Color.Black,
                inactiveColor = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

/**
 * Home页面内容
 */
@Composable
fun HomePageContent(
    viewState: HomePageViewState,
    pagePosition: Int,
    modifier: Modifier = Modifier
) {
    when(pagePosition) {
        1 -> {
            RightLeftOneCenterTwo(
                viewState = viewState,
                modifier = modifier
            )
        }
        2 -> {
            RightTwoLeftOne(
                viewState = viewState,
                modifier = modifier
            )
        }
        else -> {
            RightOneLeftTwo(
                viewState = viewState,
                modifier = modifier
            )
        }

    }
}

@Composable
private fun RightLeftOneCenterTwo(
    viewState: HomePageViewState,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 15.cdp,
                end = 30.cdp,
                start = 30.cdp
            )
    ) {
        val(
            centerContentView,
            leftContentView,
            rightContentView
        ) = createRefs()

        val contentRadius = 5.cdp
        val leftColorsBrush = remember {
            brush121_192
        }

        // 左边图片
        TagImageView(
            advert = viewState.dataPage2.getOrNull(0),
            modifier = Modifier
                .constrainAs(leftContentView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                }
                .border(
                    BorderStroke(contentRadius, leftColorsBrush),
                    RoundedCornerShape(contentRadius)
                )
                .clip(RoundedCornerShape(contentRadius))
                .fillMaxSize(0.25f)
        )

        // 右边图片
        TagImageView(
            advert = viewState.dataPage2.getOrNull(3),
            modifier = Modifier
                .constrainAs(rightContentView) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                }
                .border(
                    BorderStroke(contentRadius, leftColorsBrush),
                    RoundedCornerShape(contentRadius)
                )
                .clip(RoundedCornerShape(contentRadius))
                .fillMaxSize(0.25f)
        )

        // 中间
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.cdp),
            modifier = Modifier
                .constrainAs(centerContentView) {
                    start.linkTo(leftContentView.end)
                    end.linkTo(rightContentView.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(
                    horizontal = 30.cdp
                )
        ) {
            TagImageView(
                advert = viewState.dataPage2.getOrNull(1),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .border(
                        BorderStroke(contentRadius, leftColorsBrush),
                        RoundedCornerShape(contentRadius)
                    )
                    .clip(RoundedCornerShape(contentRadius))
            )

            TagImageView(
                advert = viewState.dataPage2.getOrNull(2),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .border(
                        BorderStroke(contentRadius, leftColorsBrush),
                        RoundedCornerShape(contentRadius)
                    )
                    .clip(RoundedCornerShape(contentRadius))
            )
        }
    }
}

@Composable
private fun RightTwoLeftOne(
    viewState: HomePageViewState,
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
                leftContentView,
                rightContentView
            ) = createRefs()

            // 竖中线
            val guideLineV = createGuidelineFromStart(0.5f)
            // 底部中线
            val guideLine9f = createGuidelineFromTop(1f)

            val contentRadius = 5.cdp

            // 左边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        linkTo(
                            end = guideLineV,
                            start = parent.start
                        )
                        top.linkTo(parent.top)
                        bottom.linkTo(guideLine9f)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        end = 15.cdp
                    )
            ) {
                val leftColorsBrush = remember {
                    brush121_192
                }

                val (
                    topIconView,
                    bottomIconView,
                ) = createRefs()

                TagImageView(
                    advert = viewState.dataPage3.getOrNull(0),
                    modifier = Modifier
                        .border(
                            BorderStroke(contentRadius, leftColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxSize()
                )

                CommonIcon(
                    resId = R.drawable.ic_image_top_start,
                    tint = Color(121, 247, 255),
                    modifier = Modifier
                        .constrainAs(topIconView) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .graphicsLayer {
                            this.translationX = (-10).cdp.toPx
                            this.translationY = (-1).cdp.toPx
                        }
                        .width(64.cdp)
                        .height(54.cdp)
                        .zIndex(9f)
                )

                CommonIcon(
                    resId = R.drawable.ic_image_bottom_start,
                    tint = Color(121, 247, 255),
                    modifier = Modifier
                        .constrainAs(bottomIconView) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                        .graphicsLayer {
                            this.translationX = (-2).cdp.toPx
                            this.translationY = (2).cdp.toPx
                        }
                        .width(66.cdp)
                        .height(56.cdp)
                        .zIndex(9f)
                )
            }

            // 右边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(rightContentView) {
                        linkTo(end = parent.end, start = guideLineV)
                        linkTo(
                            top = parent.top,
                            bottom = guideLine9f
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 15.cdp,
                    )
            ) {
                val (
                    topView,
                    bottomView,
                ) = createRefs()

                val lineLeftContentH = createGuidelineFromTop(0.5f)

                val topColorsBrush = remember {
                    brush121
                }
                val bottomColorsBrush = remember {
                    brush247
                }

                TagImageView(
                    advert = viewState.dataPage3.getOrNull(1),
                    modifier = Modifier
                        .constrainAs(topView) {
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
                        .border(
                            BorderStroke(contentRadius, topColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )

                TagImageView(
                    advert = viewState.dataPage3.getOrNull(2),
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
                        .border(
                            BorderStroke(contentRadius, bottomColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )
            }
        }
    }
}

/**
 * 测试图片地址
 * https://img.js.design/assets/img/617a65656047a179b22639e6.png
 * https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png
 * https://img.js.design/assets/img/64b4d72aa669203171642f06.png
 */
@Composable
private fun RightOneLeftTwo(
    viewState: HomePageViewState,
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
                leftContentView,
                rightContentView
            ) = createRefs()

            // 竖中线
            val guideLineV = createGuidelineFromStart(0.5f)
            // 底部中线
            val guideLine9f = createGuidelineFromTop(1f)

            val contentRadius = 5.cdp

            // 右边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(rightContentView) {
                        linkTo(
                            start = guideLineV,
                            end = parent.end
                        )
                        top.linkTo(parent.top)
                        bottom.linkTo(guideLine9f)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 15.cdp
                    )
            ) {
                val leftColorsBrush = remember {
                    brush121_192
                }
                TagImageView(
                    advert = viewState.dataPage1.getOrNull(0),
                    modifier = Modifier
                        .border(
                            BorderStroke(contentRadius, leftColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxSize()
                )
            }

            // 左边图片布局
            ConstraintLayout(
                modifier = Modifier
                    .constrainAs(leftContentView) {
                        linkTo(start = parent.start, end = guideLineV)
                        linkTo(
                            top = parent.top,
                            bottom = guideLine9f
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(
                        end = 15.cdp,
                    )
            ) {
                val (
                    topView,
                    topIconView,
                    bottomView,
                    bottomIconView,
                ) = createRefs()

                val lineLeftContentH = createGuidelineFromTop(0.5f)

                val topColorsBrush = remember {
                    brush121
                }
                val bottomColorsBrush = remember {
                    brush247
                }

                CommonIcon(
                    resId = R.drawable.ic_image_top_start,
                    tint = Color(121, 247, 255),
                    modifier = Modifier
                        .constrainAs(topIconView) {
                            top.linkTo(topView.top)
                            start.linkTo(topView.start)
                        }
                        .graphicsLayer {
                            this.translationX = (-10).cdp.toPx
                            this.translationY = (-1).cdp.toPx
                        }
                        .width(64.cdp)
                        .height(54.cdp)
                        .zIndex(9f)
                )

                CommonIcon(
                    resId = R.drawable.ic_image_bottom_start,
                    tint = Color(121, 247, 255),
                    modifier = Modifier
                        .constrainAs(bottomIconView) {
                            bottom.linkTo(bottomView.bottom)
                            start.linkTo(bottomView.start)
                        }
                        .graphicsLayer {
                            this.translationX = (-2).cdp.toPx
                            this.translationY = (2).cdp.toPx
                        }
                        .width(66.cdp)
                        .height(56.cdp)
                        .zIndex(9f)
                )

                TagImageView(
                    advert = viewState.dataPage1.getOrNull(1),
                    modifier = Modifier
                        .constrainAs(topView) {
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
                        .border(
                            BorderStroke(contentRadius, topColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )

                TagImageView(
                    advert = viewState.dataPage1.getOrNull(2),
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
                        .border(
                            BorderStroke(contentRadius, bottomColorsBrush),
                            RoundedCornerShape(contentRadius)
                        )
                        .clip(RoundedCornerShape(contentRadius))
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun TagImageView(
    advert: Advert?,
    modifier: Modifier,
    typeOnClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        CommonNetworkImage(
            url = advert?.image ?: "",
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.cdp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.BottomStart)
                .padding(
                    bottom = 25.cdp,
                    start = 25.cdp
                )
        ) {
            advert?.title?.let {
                CommonText(
                    it,
                    color = Color.Black,
                    fontSize = 25.csp
                )
            }
            advert?.subTitle?.let {
                CommonText(
                    it,
                    maxLines = 3,
                    color = Color.Black,
                    lineHeight = 35.csp,
                    fontSize = 25.csp
                )
            }
        }

        when(advert?.advertType) {
            AdvertType.PPC -> {
                TextBackground(
                    text = "点击进入",
                    fontSize = 28.csp,
                    textColor = Color.White,
                    textHorizontalPadding = 20.cdp,
                    textBackgroundBrush = AppConfig.CustomButtonBrushGreen,
                    shapes = Shapes.extraLarge,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 40.cdp,
                            bottom = 20.cdp
                        )
                ) {
                    // test url : https://baidu.com
                    // val url = URLEncoder.encode(advert.url ?: "https://www.ctrip.com/?sid=155952&allianceid=4897&ouid=index", StandardCharsets.UTF_8.toString())

                    // AppNavController.instance.navigate("${Route.WEB_VIEW}/${advert.title}/${url}")
                }
            }
            AdvertType.PPA -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.cdp)
                ) {
                    CommonNetworkImage(
                        url = advert.qrCode,
                        modifier = Modifier
                            .fillMaxWidth(0.16f)
                            .aspectRatio(1f)
                            .border(
                                width = 9.cdp,
                                color = Color(64, 239, 180)
                            )
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun VideoOrImageView(
    url: String,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier
) {
    // 透明度是否逐步增大
    val alphaIncrease = remember {
        mutableStateOf(false)
    }

    val animatorDuration = 1000
    // 透明度动画，当showBigImageStatus为true也便是由小变大时，targetValue应该是1，反之则为0
    // animationSpec设置的是2s的时长
    val alpha = animateFloatAsState(
        targetValue = if (alphaIncrease.value) 1F else 0F,
        label = "",
        animationSpec = tween(animatorDuration)
    )
    // 大图x轴的偏移量
    val bigImageOffsetX = remember {
        mutableStateOf(0F)
    }
    // 大图y轴的偏移量
    val bigImageOffsetY = remember {
        mutableStateOf(0F)
    }
    // 大图宽度
    val bigImageSizeWidth = remember {
        mutableStateOf(0)
    }
    // 大图高度
    val bigImageSizeHeight = remember {
        mutableStateOf(0)
    }


    CommonNetworkImage(
        url = url,
        contentScale = ContentScale.Crop,
        modifier = modifier
            /*.onGloballyPositioned {
                val rect = it.boundsInRoot()
                val offset = Offset(rect.left, rect.top)
                BigImageManager.cellOffsetMap[index] = offset
                cellSize.value = it.size
            }*/
    )
}
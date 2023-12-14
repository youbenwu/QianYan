package com.qianyanhuyu.app_large.ui.page

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
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
import com.qianyanhuyu.app_large.model.Video
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.compose.HomeAdvertGridView
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.page.media.VideoPlayer
import com.qianyanhuyu.app_large.ui.page.media.VideoPlayerSource
import com.qianyanhuyu.app_large.ui.page.media.rememberVideoPlayerController
import com.qianyanhuyu.app_large.ui.page.media.util.MinimizeLayoutValue
import com.qianyanhuyu.app_large.ui.page.media.util.rememberMinimizeLayoutState
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.HomePageViewAction
import com.qianyanhuyu.app_large.viewmodel.HomePageViewEvent
import com.qianyanhuyu.app_large.viewmodel.HomePageViewModel
import com.qianyanhuyu.app_large.viewmodel.HomePageViewState
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

@UnstableApi
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
            10000
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
                    HomeAdvertGridView(
                        data = viewModel.viewStates.dataPage1,
                        modifier =Modifier
                            .fillMaxSize()
                    )
//                    HomePageContent(
//                        viewState = viewModel.viewStates,
//                        pagePosition = pagePosition,
//                        onViewClick = {
//                            viewModel.dispatch(HomePageViewAction.OpenViewContent(it))
//                        },
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .graphicsLayer {
//                                val pageOffset =
//                                    ((pagerState.currentPage - pagePosition) + pagerState.currentPageOffset).absoluteValue
//
//                                val transformation =
//                                    lerp(
//                                        start = 0.7f,
//                                        stop = 1f,
//                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                                    )
//                                alpha = transformation
//                                scaleY = transformation
//                            }
//                    )
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
@OptIn(ExperimentalMaterialApi::class)
@UnstableApi
@Composable
fun HomePageContent(
    viewState: HomePageViewState,
    pagePosition: Int,
    onViewClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedVideoState by rememberSaveable { mutableStateOf<Video?>(null) }

    val videoPlayerController = rememberVideoPlayerController()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(videoPlayerController, lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                videoPlayerController.pause()
            }

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)

                // 视频测试数据
                selectedVideoState = Video(
                    description = "test desc",
                    title = "tt",
                    subtitle = "subtt",
                    sources = listOf(
                        "http://tengdamy.cn/video/video2.mp4",
                        "http://tengdamy.cn/video/video2.mp4"
                    ),
                    thumb = "https://dimg04.c-ctrip.com/images/0zg2912000chbi5si501F.jpg"
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val minimizeLayoutState = rememberMinimizeLayoutState(MinimizeLayoutValue.Expanded)
    LaunchedEffect(selectedVideoState) {
        val selectedVideo = selectedVideoState
        if (selectedVideo != null) {
            videoPlayerController.setSource(VideoPlayerSource.Network(selectedVideo.sources.first()), true)
            minimizeLayoutState.expand()
        } else {
            minimizeLayoutState.hide()
            videoPlayerController.reset()
        }
    }

    // TODO 对于视频适应不同显示样式,还需调整
    when(pagePosition) {
        1 -> {
            RightLeftOneCenterTwo(
                viewState = viewState,
                onViewClick = onViewClick,
                videoContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        VideoPlayer(
                            videoPlayerController = videoPlayerController,
                            backgroundColor = Color.Transparent,
                            controlsEnabled = false,
                            gesturesEnabled = false,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                },
                modifier = modifier
            )
        }
        2 -> {
            RightTwoLeftOne(
                viewState = viewState,
                onViewClick = onViewClick,
                videoContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        VideoPlayer(
                            videoPlayerController = videoPlayerController,
                            backgroundColor = Color.Transparent,
                            controlsEnabled = false,
                            gesturesEnabled = false,
                            modifier = Modifier
                        )
                    }
                },
                modifier = modifier
            )
        }
        else -> {
            RightOneLeftTwo(
                viewState = viewState,
                onViewClick = onViewClick,
                videoContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        VideoPlayer(
                            videoPlayerController = videoPlayerController,
                            backgroundColor = Color.Transparent,
                            controlsEnabled = false,
                            gesturesEnabled = false,
                            modifier = Modifier
                        )
                    }
                },
                modifier = modifier
            )
        }

    }
}

@Composable
private fun RightLeftOneCenterTwo(
    viewState: HomePageViewState,
    onViewClick: (
        String
    ) -> Unit = {},
    videoContent: @Composable (() -> Unit)? = null,
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
            typeOnClick = onViewClick,
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
            typeOnClick = onViewClick,
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
                typeOnClick = onViewClick,
                videoContent = videoContent,
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
                typeOnClick = onViewClick,
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
    onViewClick: (
        String
    ) -> Unit = {},
    videoContent: @Composable (() -> Unit)? = null,
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
                    typeOnClick = onViewClick,
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
                    typeOnClick = onViewClick,
                    videoContent = videoContent,
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
                    typeOnClick = onViewClick,
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
    onViewClick: (
        String
    ) -> Unit = {},
    videoContent: @Composable() (() -> Unit)? = null,
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
                    typeOnClick = onViewClick,
                    videoContent = videoContent,
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
                    typeOnClick = onViewClick,
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
                    typeOnClick = onViewClick,
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
    videoContent: (@Composable () -> Unit)? = null,
    typeOnClick: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        if(videoContent == null) {
            CommonNetworkImage(
                url = advert?.image ?: "",
                modifier = Modifier
                    .fillMaxSize()
                    .onClick {
                        typeOnClick.invoke(advert?.video ?: (advert?.image ?: ""))
                    }
            )
        } else {
            videoContent()
        }

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
                    // WebView不支持https中存在http的链接
                    // TODO 测试使用固定地址，使用接口数据直接替换成url即可
                    advert.url?.let { url ->
                        val urlEncode = URLEncoder.encode( "https://www.baidu.com", StandardCharsets.UTF_8.toString())

                        AppNavController.instance.navigate("${Route.WEB_VIEW}/${advert.title}/${urlEncode}")
                    }

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
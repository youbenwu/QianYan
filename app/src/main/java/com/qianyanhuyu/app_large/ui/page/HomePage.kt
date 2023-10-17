package com.qianyanhuyu.app_large.ui.page


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.brush121
import com.qianyanhuyu.app_large.constants.AppConfig.brush121_192
import com.qianyanhuyu.app_large.constants.AppConfig.brush247
import com.qianyanhuyu.app_large.ui.page.common.NavigationDrawerSample
import com.qianyanhuyu.app_large.ui.page.groupchat.GroupChats
import com.qianyanhuyu.app_large.ui.widgets.BaseMsgDialog
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.MultiFabItem
import com.qianyanhuyu.app_large.ui.widgets.MultiFloatingActionButton
import com.qianyanhuyu.app_large.util.FormatterEnum
import com.qianyanhuyu.app_large.util.TimeUtil
import com.qianyanhuyu.app_large.util.TwoBackFinish
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.HomePageViewEvent
import com.qianyanhuyu.app_large.viewmodel.HomePageViewModel
import kotlinx.coroutines.launch
import java.util.Date

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/15 9:19
 * @Description : 主页
 */

private lateinit var openDialog: MutableState<Boolean>

private lateinit var listData: MutableState<List<String>>

@OptIn(ExperimentalPagerApi::class)
private lateinit var pagerState: PagerState

// 首页导航内容
private val expandFbItemList: MutableList<MultiFabItem> = mutableListOf(
    MultiFabItem(
        index = 0,
        icon = R.drawable.ic_nav_customer_service,
        label = "客服服务"
    ),
    MultiFabItem(
        index = 1,
        icon = R.drawable.ic_ip_put_in,
        label = "IP投放"
    ),
    MultiFabItem(
        index = 2,
        icon = R.drawable.ic_nav_travel,
        label = "智慧旅游"
    ),
    MultiFabItem(
        index = 3,
        icon = R.drawable.ic_nav_qianyan_play,
        label = "迁眼互娱",
    ),
    MultiFabItem(
        index = 4,
        icon = R.drawable.ic_nav_store,
        label = "店友圈"
    ),
    MultiFabItem(
        index = 5,
        icon = R.drawable.ic_nav_qianyan_give,
        label = "迁眼送",
    ),
)

private var selectedHomeTabIndex by mutableStateOf(expandFbItemList.size + 1)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomePageScreen(
    viewModel: HomePageViewModel = hiltViewModel(),
    onFinish: () -> Unit = {}
) {
    val context = LocalContext.current

    val castingState = viewModel.castingStateLiveData
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineState = rememberCoroutineScope()
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val selectState = remember { mutableStateOf(expandFbItemList.size + 1) }

    BackHandler {
        val homeIndex = expandFbItemList.size + 1
        // 不是首页的时候调回首页,是首页的时候按两次退出
        if(selectedHomeTabIndex != homeIndex) {
            coroutineState.launch {
                selectState.value = homeIndex
                pagerState.scrollToPage(homeIndex)
                selectedHomeTabIndex = homeIndex
            }
        } else {
            TwoBackFinish().execute(onFinish)
        }
    }

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        // viewModel.dispatch(ActivationViewAction.InitPageData)
        onDispose {  }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is HomePageViewEvent.NavTo) {

            }
            else if (it is HomePageViewEvent.ShowMessage) {
                println("收到错误消息：${it.message}")
                coroutineState.launch {
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    MaterialTheme {
        NavigationDrawerSample(
            drawerState = drawerState,
            sheetContent = {
                GroupChats(
                    modifier = Modifier.fillMaxSize()
                )
            }
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    HomeTopBar(
                        selectState = selectState,
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.cdp)
                    )
                },
                floatingActionButton = {
                    MultiFloatingActionButton(
                        items = expandFbItemList,
                        selectState = selectState,
                    ) { item ->
                        coroutineState.launch {
                            pagerState.scrollToPage(item.index)
                            selectedHomeTabIndex = item.index
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center
            ) { innerPadding ->
                HomePageBody(
                    drawerState = drawerState,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

/**
 * 顶部TopBar 的布局
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeTopBar(
    selectState: MutableState<Int>,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {

        val currentTime = TimeUtil.parse(Date().time, FormatterEnum.YYYY_DOT_MM_DOT_DD)

        val (
            centerView,
            centerTitleTextView,
            lineLeftView,
            lineLeftStartView,
            lineRightView,
            lineRightEndView,
            contentLeftView,
            contentRightView,
        ) = createRefs()

        CommonComposeImage(
            resId = R.drawable.top_bar_center_bg,
            modifier = Modifier
                .constrainAs(centerView) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)
                }
                .padding(
                    vertical = 17.cdp
                )
                .width(594.cdp)
                .height(85.cdp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(centerTitleTextView) {
                linkTo(start = centerView.start, end = centerView.end)
                top.linkTo(centerView.top)
                bottom.linkTo((centerView.bottom))
            }
        ) {
            Text(
                text = "欢迎使用迁眼互娱平台",
                fontWeight = FontWeight.Bold,
                fontSize = 32.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White
            )
            Text(
                text = "WELCOME TO EYE ENTERTAINMENT",
                fontWeight = FontWeight.Bold,
                fontSize = 12.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White
            )
        }

        CommonComposeImage(
            resId = R.drawable.top_bar_line,
            modifier = Modifier
                .constrainAs(lineLeftView) {
                    end.linkTo(centerView.start)
                    start.linkTo(parent.start)
                    linkTo(top = centerView.top, bottom = centerView.bottom)

                    width = Dimension.fillToConstraints
                }
                .padding(top = 7.cdp)
                .height(1.cdp)
        )

        CommonComposeImage(
            resId = R.drawable.top_bar_left_,
            modifier = Modifier
                .constrainAs(lineLeftStartView) {
                    start.linkTo(lineLeftView.start)
                    bottom.linkTo(lineLeftView.bottom)
                }
        )

        // 右边内容
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .constrainAs(contentRightView) {
                    start.linkTo(centerView.end)
                    end.linkTo(lineRightEndView.start)
                    linkTo(top = parent.top, bottom = lineRightView.top)

                    width = Dimension.preferredWrapContent
                }
                .padding(
                    top = 12.cdp
                )
        ) {
            CommonComposeImage(
                resId = R.drawable.ic_xiaoyu,
                modifier = Modifier
                    .padding(
                        start = 20.cdp,
                        end = 10.cdp
                    )
            )
            Text(
                text = "17℃",
                fontSize = 24.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White
            )

            Text(
                text = currentTime,
                fontSize = 24.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier
                    .padding(
                        start = 20.cdp,
                        end = 20.cdp
                    )
            )

            CommonIcon(
                resId = R.drawable.ic_location,
                tint = Color.White,
                modifier = Modifier
                    .size(30.cdp)
            )
            Text(
                text = "广州",
                fontSize = 24.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        start = 10.cdp,
                        end = 20.cdp
                    )
            )
        }

        CommonComposeImage(
            resId = R.drawable.top_bar_line,
            modifier = Modifier
                .constrainAs(lineRightView) {
                    start.linkTo(centerView.end)
                    end.linkTo(parent.end)
                    linkTo(top = centerView.top, bottom = centerView.bottom)

                    width = Dimension.fillToConstraints
                }
                .padding(top = 7.cdp)
                .height(1.cdp)
        )

        CommonComposeImage(
            resId = R.drawable.top_bar_right_,
            modifier = Modifier
                .constrainAs(lineRightEndView) {
                    end.linkTo(lineRightView.end)
                    bottom.linkTo(lineRightView.bottom)
                }
        )

        // 左边内容
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .constrainAs(contentLeftView) {
                    end.linkTo(centerView.start)
                    start.linkTo(lineLeftStartView.end)
                    linkTo(top = parent.top, bottom = lineLeftView.top)

                    width = Dimension.preferredWrapContent
                }
                .padding(
                    top = 10.cdp,
                    start = 20.cdp,
                    end = 20.cdp
                )
        ) {
            val ipCoroutineState = rememberCoroutineScope()

            CommonComposeImage(
                R.drawable.ic_home_button,
                modifier = Modifier
                    .width(41.cdp)
                    .height(37.cdp)
                    .clickable {
                        ipCoroutineState.launch {
                            val homeIndex = expandFbItemList.size + 1
                            selectState.value = homeIndex
                            pagerState.scrollToPage(homeIndex)
                            selectedHomeTabIndex = homeIndex
                        }
                    }
            )
            Text(
                text = "返回主页",
                fontSize = 24.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        start = 10.cdp,
                        end = 20.cdp
                    )
                    .clickable {
                        ipCoroutineState.launch {
                            val homeIndex = expandFbItemList.size + 1
                            selectState.value = homeIndex
                            pagerState.scrollToPage(homeIndex)
                            selectedHomeTabIndex = homeIndex
                        }
                    }
            )

            CommonComposeImage(
                R.drawable.ic_projection_screen,
                modifier = Modifier
                    .width(44.cdp)
                    .height(33.cdp)
                    .clickable {

                    }
            )
            Text(
                text = "互动投屏",
                fontSize = 24.csp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        start = 10.cdp
                    )
                    .clickable {

                    }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePageBody(
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier
) {
    pagerState = rememberPagerState(
        initialPage = selectedHomeTabIndex,
    )

    CommonComposeImage(
        R.drawable.home_bg,
        modifier = Modifier.fillMaxSize()
    )

    HorizontalPager(
        count = expandFbItemList.size + 2,
        state = pagerState,
        userScrollEnabled = false,
        modifier = modifier
            .fillMaxSize()
    ) { pagePosition ->
        selectedHomeTabIndex = pagerState.currentPage

        when (pagePosition) {
            0 -> CustomerServiceScreen(snackbarHostState = snackbarHostState)
            1 -> IpPutInScreen(snackbarHostState = snackbarHostState)
            2 -> SmartTourismScreen(snackbarHostState = snackbarHostState)
            3 -> QianYanPlayScreen(snackbarHostState = snackbarHostState)
            4 -> ShopFriendsScreen(snackbarHostState = snackbarHostState, drawerState = drawerState)
            5 -> QianYanGiveScreen(snackbarHostState = snackbarHostState)
            else -> HomePageContent(
                snackbarHostState = snackbarHostState
            )
        }
    }
}

/**
 * Home页面内容
 */
@Composable
fun HomePageContent(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
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
            val guideLine9f = createGuidelineFromTop(0.9f)

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
                CommonNetworkImage(
                    url = "https://img.js.design/assets/img/617a65656047a179b22639e6.png",
                    contentScale = ContentScale.Crop,
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

                CommonNetworkImage(
                    url = "https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png",
                    contentScale = ContentScale.Crop,
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

                CommonNetworkImage(
                    url = "https://img.js.design/assets/img/64b4d72aa669203171642f06.png",
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
fun testAA(
    viewModel: HomePageViewModel,
    fragmentManager: FragmentManager
) {
    Column {
        openDialog = remember {
            mutableStateOf(false)
        }

        listData = remember {
            mutableStateOf(listOf("test"))
        }

        if(viewModel.isCastingAvailable) {
            Column {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        try {
                            val shouldShowChooserFragment = viewModel.shouldShowChooserFragment()
                            val fragmentTag = if(shouldShowChooserFragment) "MediaRouteChooserFragment" else "MediaRouteControllerDialogFragment"
                            val fragment = if(shouldShowChooserFragment) {
                                viewModel.mDialogFactory.onCreateChooserDialogFragment()
                                    .apply {
                                        routeSelector = viewModel.mSelector
                                    }
                            } else {
                                viewModel.mDialogFactory.onCreateControllerDialogFragment()
                            }

                            val transaction = fragmentManager.beginTransaction()
                            transaction.add(fragment, fragmentTag)
                            transaction.commitAllowingStateLoss()
                        } catch (e: Exception) {

                        }
                    },
                ) {
                    Text(text = "互动投屏")
                }
            }
            Box(

            ) {
                Image(
                    imageVector = Icons.Filled.Face,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        openDialog.value = true
                    },
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        if(openDialog.value) {
            /*BaseMsgDialog(
                title = "连接到设备",
                message = listData.value,
                confirmText="Ok"
            ) {
                openDialog.value = false
            }*/
        }
    }
}

@Composable
fun MediaRouterCustomButton(openDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    Column {
        Button(
            onClick = {
                openDialog.value = true
            },
        ) {
            Text(text = "互动投屏")
        }
    }
}
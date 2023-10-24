package com.qianyanhuyu.app_large.ui.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.HomeNavHost
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.common.NavigationDrawerSample
import com.qianyanhuyu.app_large.ui.page.groupchat.GroupChats
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.MultiFloatingActionButton
import com.qianyanhuyu.app_large.util.FormatterEnum
import com.qianyanhuyu.app_large.util.TimeUtil
import com.qianyanhuyu.app_large.util.TwoBackFinish
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import kotlinx.coroutines.launch
import java.util.Date

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/24 14:55
 * @Description : 主体内容载体
 * 区分不同类型页面进入, 使用不同的布局方式
 */

private const val HOME_ROUTE = Route.HOME_CONTENT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavHostController,
    onFinish: () -> Unit = {},
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 返回当前route名称
    val currentRoute = navBackStackEntry?.destination?.route
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val selectState = remember { mutableStateOf(-1)}
    val coroutineState = rememberCoroutineScope()

    BackHandler {
        if(drawerState.isOpen) {
            coroutineState.launch {
                drawerState.close()
            }
        } else {
            val homeIndex = -1
            // 不是首页的时候回上一页,是首页的时候按两次退出
            if(selectState.value != homeIndex) {
                coroutineState.launch {
                    selectState.value = homeIndex
                    AppNavController.instance.popBackStack()
                    AppNavController.instance.navigate(HOME_ROUTE)
                }
            } else {
                TwoBackFinish().execute(onFinish)
            }
        }
    }


    if(!currentRoute.isNullOrBlank() && isMain(currentRoute)) {
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
                        if(isShowFloatButton(currentRoute)) {
                            MultiFloatingActionButton(
                                items = expandFbItemList,
                                selectState = selectState,
                            ) { item ->
                                AppNavController.instance.popBackStack()
                                AppNavController.instance.navigate(item.route)
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // 背景图
                        CommonComposeImage(
                            R.drawable.home_bg,
                            modifier = Modifier.fillMaxSize()
                        )

                        // 路由跳转的主体内容
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            HomeNavHost(
                                navController,
                                drawerState = drawerState
                            ) {
                                onFinish.invoke()
                            }
                        }
                    }

                }
            }


        }
    } else {
        HomeNavHost(
            navController,
            drawerState = drawerState
        ) {
            onFinish.invoke()
        }
    }
}

/**
 * 顶部TopBar 的布局
 */
@Composable
private fun HomeTopBar(
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
                            selectState.value = -1
                            AppNavController.instance.popBackStack()
                            AppNavController.instance.navigate(HOME_ROUTE)
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
                            selectState.value = -1
                            AppNavController.instance.popBackStack()
                            AppNavController.instance.navigate(HOME_ROUTE)

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

/**
 * 是否是主体页面
 */
private fun isMain(
    route: String
) : Boolean = when(route) {
    Route.SPLASH,
    Route.LOGIN,
    Route.ACTIVATION,
    Route.AUTHENTICATION -> false
    else -> true
}

/**
 * 是否显示悬浮按钮
 */
private fun isShowFloatButton(
    route: String
): Boolean = when(route) {
    Route.DRY_CLEAN,
    Route.SMART_TOURISM_DETAIL -> false
    else -> true
}
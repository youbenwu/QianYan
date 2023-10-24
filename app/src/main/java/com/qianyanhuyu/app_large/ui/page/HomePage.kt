package com.qianyanhuyu.app_large.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig.brush121
import com.qianyanhuyu.app_large.constants.AppConfig.brush121_192
import com.qianyanhuyu.app_large.constants.AppConfig.brush247
import com.qianyanhuyu.app_large.model.MultiMenuItem
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.HomePageViewEvent
import com.qianyanhuyu.app_large.viewmodel.HomePageViewModel
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/15 9:19
 * @Description : 主页
 */

private lateinit var openDialog: MutableState<Boolean>

private lateinit var listData: MutableState<List<String>>

// 首页导航内容
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

private var selectedHomeTabIndex by mutableStateOf(expandFbItemList.size + 1)

@Composable
fun HomePageScreen(
    viewModel: HomePageViewModel = hiltViewModel(),
    navController: NavHostController,
    onFinish: () -> Unit = {}
) {
    val context = LocalContext.current

    val castingState = viewModel.castingStateLiveData
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineState = rememberCoroutineScope()
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

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

    HomePageContent(
        modifier = Modifier
            .fillMaxSize()
    )

}

/**
 * Home页面内容
 */
@Composable
fun HomePageContent(
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
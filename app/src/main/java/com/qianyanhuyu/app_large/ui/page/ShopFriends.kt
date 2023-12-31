package com.qianyanhuyu.app_large.ui.page

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.ImageCircle
import com.qianyanhuyu.app_large.ui.page.common.ShopFriendsAnimation
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.units
import com.qianyanhuyu.app_large.viewmodel.ShopFriendsViewAction
import com.qianyanhuyu.app_large.viewmodel.ShopFriendsViewEvent
import com.qianyanhuyu.app_large.viewmodel.ShopFriendsViewModel
import com.qianyanhuyu.app_large.viewmodel.ShopFriendsViewState
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/25 9:19
 * @Description : 店友圈
 */
private lateinit var timerImage: Timer
private lateinit var timerOnlinePerson: Timer

@Composable
fun ShopFriendsScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: ShopFriendsViewModel = hiltViewModel()
) {

    val coroutineState = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                //根据Event执行不同生命周期的操作
                Lifecycle.Event.ON_CREATE -> {
                    timerImage = Timer()
                    timerOnlinePerson = Timer()
                }
                Lifecycle.Event.ON_START -> {
                    viewModel.dispatch(ShopFriendsViewAction.InitPageData)
                    // 图片
                    timerImage.schedule(
                        object : TimerTask() {
                            override fun run() {
                                viewModel.dispatch(ShopFriendsViewAction.UpdateImageData)
                            }
                        },
                        2000,
                        3000
                    )
                    // 人数
                    timerOnlinePerson.schedule(
                        object : TimerTask() {
                            override fun run() {
                                viewModel.dispatch(ShopFriendsViewAction.UpdateOnlinePerson)
                            }
                        },
                        3000,
                        3000
                    )
                }
                else -> {
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            timerImage.cancel()
            timerOnlinePerson.cancel()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is ShopFriendsViewEvent.ShowMessage) {
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
        ShopFriendsContent(
            viewStates = viewModel.viewStates,
            onGroupChat = {
                viewModel.dispatch(ShopFriendsViewAction.UpdateOnlinePerson)
            },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

/**
 * ShopFriends页面内容
 */
@Composable
fun ShopFriendsContent(
    onGroupChat: () -> Unit = {},
    viewStates: ShopFriendsViewState,
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
                numberCountTripText,
                numberCountView,
                matchButton,
                groupChatButton,
            ) = createRefs()

            /*Text("test")*/
            val colorPurple = Color(141, 128, 255).copy(alpha = 1f)
            val colorBlue = Color(158, 255, 255).copy(alpha = 1f)


            ShopFriendsButton(
                "妙音匹配",
                textColors = listOf(
                    colorPurple,
                    colorBlue
                ),
                iconId = R.drawable.icon_group_match,
                textStartPadding = 25.cdp,
                iconStartPadding = 25.cdp,
                modifier = Modifier
                    .constrainAs(matchButton) {
                        start.linkTo(parent.start, margin = 30.cdp)
                        bottom.linkTo(groupChatButton.top, margin = 100.cdp)
                    }
                    .width(380.cdp)
                    .height(145.cdp)
                    .background(Color(27, 126, 242).copy(alpha = 0.2f))
                    .border(
                        BorderStroke(1.cdp, Color(0, 102, 255)),
                        RoundedCornerShape(4.cdp)
                    )
                    .clickable {

                    }
            )

            ShopFriendsButton(
                "建立群聊",
                textColors = listOf(
                    colorBlue,
                    colorPurple
                ),
                iconId = R.drawable.icon_group_chat,
                modifier = Modifier
                    .constrainAs(groupChatButton) {
                        start.linkTo(parent.start, margin = 30.cdp)
                        bottom.linkTo(parent.bottom, margin = 125.cdp)
                    }
                    .width(380.cdp)
                    .height(145.cdp)
                    .background(Color(24, 254, 254).copy(alpha = 0.2f))
                    .border(
                        BorderStroke(1.cdp, Color(24, 254, 254)),
                        RoundedCornerShape(4.cdp)
                    )
                    .clickable {
                        onGroupChat.invoke()
                    }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(numberCountView) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 175.cdp)

                        height = Dimension.preferredWrapContent
                    }
            ) {
                viewStates.onlinePersonCount.units().forEach {
                    NumberCount(
                        it,
                        modifier = Modifier
                            .padding(
                                end = 10.cdp
                            )
                    )
                }
            }

            CommonText(
                "今日在线人数",
                fontSize = 30.csp,
                textAlign = TextAlign.Right,
                letterSpacing = 5.csp,
                modifier = Modifier
                    .constrainAs(numberCountTripText) {
                        end.linkTo(numberCountView.end)
                        bottom.linkTo(numberCountView.top, margin = 10.cdp)
                    }
                    .padding(
                        end = 15.cdp
                    )
            )

            RadarView(
                viewStates = viewStates,
                modifier = Modifier.fillMaxSize()
            )

        }
    }
}

@Composable
private fun RadarView(
    viewStates: ShopFriendsViewState,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                top = 20.cdp,
                bottom = 20.cdp
            )
    ) {
        ShopFriendsAnimation(
            modifier = Modifier
                .fillMaxSize()
        )

        CommonComposeImage(
            resId = R.drawable.icon_ip_put_center,
            modifier = Modifier
                .zIndex(9F)
        )

        viewStates.imageData.forEachIndexed { _, imageData ->
            imageData.image?.let { bitmap ->
                ImageCircle(
                    name = imageData.name,
                    imgSrc = bitmap,
                    delay = imageData.imageDelay,
                    circleImageState = imageData.isShow,
                    circleSize = imageData.imageSize,
                    circleAngle = imageData.angle,
                    circleOffsetIndex = imageData.offsetIndex,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ShopFriendsButton(
    text: String,
    @DrawableRes iconId: Int,
    textStartPadding: Dp = 0.cdp,
    iconStartPadding: Dp = 0.cdp,
    textColors: List<Color>? = null,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            CommonComposeImage(
                resId = iconId,
                modifier = Modifier
                    .padding(
                        start = iconStartPadding
                    )
            )

            Text(
                text,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = textColors ?: listOf(Color.White),
                        tileMode = TileMode.Mirror
                    ),
                    fontSize = 35.csp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.csp
                ),
                modifier = Modifier
                    .padding(
                        start = textStartPadding
                    )
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun NumberCount(
    textLabel: Int,
    modifier: Modifier
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(56.cdp)
    ) {
        CommonComposeImage(
            resId = R.drawable.number_count_bg
        )

        AnimatedContent(
            targetState = textLabel,
            transitionSpec = {
                addAnimation().using(SizeTransform(clip = true))
            }, label = ""
        ) { targetCount ->
            Text(
                "$targetCount",
                fontSize = 35.csp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.csp,
                color = Color(0, 255, 243),
            )
        }
    }
}

/**
 * 数字更改动画效果
 */
@ExperimentalAnimationApi
private fun addAnimation(duration: Int = 800): ContentTransform {
    return slideInVertically(
        animationSpec = tween(duration)
    ) { height ->
        height
    } + fadeIn(
        animationSpec = tween(duration)
    ) with slideOutVertically(
        animationSpec = tween(duration)
    ) { height ->
        height
    } + fadeOut(
        animationSpec = tween(duration)
    )
}
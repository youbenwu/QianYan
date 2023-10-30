package com.qianyanhuyu.app_large.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.model.Product
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.viewmodel.DryCleanViewAction
import com.qianyanhuyu.app_large.viewmodel.DryCleanViewModel
import com.qianyanhuyu.app_large.viewmodel.DryCleanViewState
import kotlinx.coroutines.launch

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/24 13:24
 * @Description : 干洗服务
 */

@Composable
fun DryClean(
    viewModel: DryCleanViewModel = hiltViewModel()
) {

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(DryCleanViewAction.InitPageData)
        onDispose {  }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DryCleanContent(
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DryCleanContent(
    viewState: DryCleanViewState,
    modifier: Modifier
) {
    val labelCoroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val selectedIndex = remember {
        mutableStateOf(0)
    }

    Column(
        modifier = modifier
    ) {
        // 分类栏
        Row(
            horizontalArrangement = Arrangement.spacedBy(30.cdp, Alignment.Start),
            modifier = Modifier
                .padding(50.cdp)
        ) {
            CommonText(
                "分类选择:",
            )

            viewState.typeData.forEach {
                val isCheck = it.type == selectedIndex.value

                TextBackground(
                    text = it.name,
                    textBackground = Color.Transparent,
                    textBackgroundBrush = if(isCheck) { AppConfig.ipPutInBorder } else { null },
                    fontSize = 25.csp,
                    textHorizontalPadding = 50.cdp,
                    modifier = Modifier
                        .border(
                            if (isCheck) {
                                0.cdp
                            } else {
                                1.cdp
                            },
                            Color.White,
                            Shapes.extraSmall
                        )
                        .clip(Shapes.extraSmall)
                ) {
                    labelCoroutineScope.launch {
                        selectedIndex.value = it.type
                        pagerState.scrollToPage(it.type)
                    }
                }
            }
        }

        // 分类内容页面
        HorizontalPager(
            count = viewState.data.count(),
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier
                .weight(1f)
        ) { pagePosition ->
            TypeContentView(
                data = viewState.data[pagePosition],
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

/**
 * 分类操作内容页
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TypeContentView(
    data: Product,
    modifier: Modifier
) {
    val imageData = listOf(
        "https://img.js.design/assets/img/6520baa5d06eb3d57dfef66f.png",
        "https://img.js.design/assets/img/6520baa5d06eb3d57dfef66f.png",
        "https://img.js.design/assets/img/6520baa5d06eb3d57dfef66f.png"
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    ConstraintLayout(
        modifier = modifier
    ) {

        val (
            leftImageView,
            rightContentView,
        ) = createRefs()

        val guideLine = createGuidelineFromStart(0.33f)

        Column(
            modifier = Modifier
                .constrainAs(leftImageView) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    start.linkTo(parent.start)
                    end.linkTo(guideLine)


                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(
                    start = 80.cdp,
                    bottom = 20.cdp
                )
        ) {
            HorizontalPager(
                count = imageData.count(),
                state = pagerState,
                userScrollEnabled = true,
                modifier = modifier
                    .weight(1f)
            ) { pagePosition ->
                CommonNetworkImage(
                    url = imageData[pagePosition],
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            // 滑动指示器
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 45.cdp
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

        RightContentView(
            data = data,
            modifier = Modifier
                .constrainAs(rightContentView) {
                    linkTo(
                        start = guideLine,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.preferredWrapContent
                }
                .padding(
                    horizontal = 150.cdp
                )
        )
    }
}

@Composable
private fun RightContentView(
    data: Product,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(50.cdp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                CommonText(
                    "| ${data.title}",
                    textAlign = TextAlign.Start,
                    fontSize = 50.csp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(3f)
                )
                CommonText(
                    "¥ 599.00",
                    color = AppConfig.CustomOrigin,
                    fontSize = 50.csp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Divider(
                color = Color.White.copy(alpha = 0.8f),
                thickness = 1.cdp
            )

            CommonText(
                "| 附加项",
                fontSize = 50.csp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AdditionalItem(
                    typeName = "皮面修复翻新",
                    price = "300.00",
                    modifier = Modifier
                        .weight(1f)
                )
                AdditionalItem(
                    typeName = "缝线保色",
                    price = "100.00",
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 60.cdp
                        )
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AdditionalItem(
                    typeName = "皮面改色",
                    price = "200.00",
                    modifier = Modifier
                        .weight(1f)
                )
                AdditionalItem(
                    typeName = "高奢品牌",
                    price = "100.00",
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 60.cdp
                        )
                )
            }

            ConfirmButton(
                "立即支付",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .weight(3f)
            ) {

            }
        }
    }
}


@Composable
private fun ConfirmButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    AppConfig.ipPutInBorder,
                    shape = Shapes.extraLarge
                )
                .clip(shape = Shapes.extraLarge)
                .padding(
                    vertical = 30.cdp
                )
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)
                .onClick {
                    onClick.invoke()
                }
        ) {
            CommonText(
                text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AdditionalItem(
    typeName: String,
    price: String,
    modifier: Modifier
) {
    CommonText(
        "· $typeName",
        textAlign = TextAlign.Start,
        modifier = modifier
    )
    CommonText(
        "+ ¥ $price",
        textAlign = TextAlign.Start,
    )
}

@Preview
@Composable
fun PreView() {
    DryClean()
}
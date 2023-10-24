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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/24 13:24
 * @Description : 干洗服务
 */

@Composable
fun DryClean() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DryCleanContent(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

data class DryCleanType(
    val name: String,
    val type: Int,
)

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DryCleanContent(
    modifier: Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
    )

    val testTypeData = listOf<DryCleanType>(
        DryCleanType(
            name = "服饰",
            type = 1,
        ),
        DryCleanType(
            name = "家访",
            type = 2,
        ),
        DryCleanType(
            name = "皮衣",
            type = 3,
        ),
        DryCleanType(
            name = "箱包",
            type = 4,
        )
    )


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

            testTypeData.forEach {
                TextBackground(
                    text = it.name,
                    textBackground = Color.Transparent,
                    fontSize = 25.csp,
                    textHorizontalPadding = 50.cdp,
                    modifier = Modifier
                        .border(
                            1.cdp,
                            Color.White
                        )
                        .clip(Shapes.extraSmall)
                )
            }
        }

        // 分类内容页面
        HorizontalPager(
            count = testTypeData.count(),
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier
                .weight(1f)
        ) { _ ->
            TypeContentView(
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

        Box(
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
        ) {
            HorizontalPager(
                count = imageData.count(),
                state = pagerState,
                userScrollEnabled = true,
                modifier = modifier
                    .fillMaxSize()
            ) { pagePosition ->
                CommonNetworkImage(
                    url = imageData[pagePosition],
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.cdp)
                )
            }

            // 滑动指示器
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.cdp
                    )
                    .align(Alignment.BottomCenter)
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
        )
    }
}

@Composable
private fun RightContentView(
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
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonText(
                    "| 皮上衣",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(3f)
                )
                CommonText(
                    "¥ 599.0",
                    color = AppConfig.CustomOrigin,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            CommonText(
                "| 附加项",
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
                )
            }

            ConfirmButton(
                "立即支付",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Composable
private fun ConfirmButton(
    text: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                AppConfig.ipPutInBorder,
                shape = Shapes.extraLarge
            )
            .clip(shape = Shapes.extraLarge)
            .padding(
                vertical = 30.cdp
            )
    ) {
        CommonText(
            text,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )
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
        modifier = modifier
    )
}

@Preview
@Composable
fun PreView() {
    DryClean()
}
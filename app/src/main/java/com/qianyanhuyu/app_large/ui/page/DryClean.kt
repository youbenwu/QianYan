package com.qianyanhuyu.app_large.ui.page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.model.Product
import com.qianyanhuyu.app_large.data.model.ProductAttributes
import com.qianyanhuyu.app_large.ui.page.common.CircleCheckbox
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.ui.widgets.SimpleEditTextWidget
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
            countOnClick = { calculate, product ->
                viewModel.dispatch(DryCleanViewAction.CountChange(calculate, product))
            },
            onChecked = { productId, id, isCheck ->
                viewModel.dispatch(DryCleanViewAction.CheckItem(
                    id = id,
                    productId = productId,
                    isCheck = isCheck
                ))
            },
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
    countOnClick: (
        AppConfig.Calculate,
        Product
    ) -> Unit,
    onChecked: (
        Int,Int,Boolean
    ) -> Unit,
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
                countOnClick = countOnClick,
                onChecked = onChecked,
                checkItems = viewState.checkItems,
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
    checkItems: List<List<ProductAttributes>?>?,
    countOnClick: (
        AppConfig.Calculate,
        Product
    ) -> Unit,
    onChecked: (
        Int,Int,Boolean
    ) -> Unit,
    modifier: Modifier
) {
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
                count = data.details?.images?.count() ?: 0,
                state = pagerState,
                userScrollEnabled = true,
                modifier = modifier
                    .weight(1f)
            ) { pagePosition ->
                CommonNetworkImage(
                    url = data.details?.images?.get(pagePosition)?.url ?: "",
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
            checkItems = checkItems,
            countOnClick = countOnClick,
            onChecked = onChecked,
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
                    horizontal = 60.cdp
                )
        )
    }
}

@Composable
private fun RightContentView(
    data: Product,
    checkItems: List<List<ProductAttributes>?>?,
    countOnClick: (
        AppConfig.Calculate,
        Product
    ) -> Unit,
    onChecked: (
        Int,
        Int,
        Boolean
    ) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(0.cdp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(25.cdp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonText(
                    "¥ ${data.price}",
                    color = AppConfig.CustomOrigin,
                    fontSize = 50.csp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                )

                CommonIcon(
                    resId = R.drawable.ic_minus,
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.cdp)
                        .onClick {
                            countOnClick.invoke(AppConfig.Calculate.MINUS, data)
                        }
                )

                /*SimpleEditTextWidget(
                    value = "1",
                    keyboardType = KeyboardType.Number,
                    isReadOnly = true,
                    isShowTrailingIcon = false,
                    modifier = Modifier
                        .size(80.cdp)
                )*/

                CommonText(
                    "${data.countProduct ?: 1}",
                    fontSize = 45.csp,
                    color = Color.White,
                    modifier = Modifier
                        .defaultMinSize(
                            80.cdp
                        )
                )

                CommonIcon(
                    resId = R.drawable.ic_add,
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.cdp)
                        .onClick {
                            countOnClick.invoke(AppConfig.Calculate.ADD, data)
                        }
                )


                CommonText(
                    "件",
                    fontSize = 45.csp,
                    textAlign = TextAlign.Center,
                )
            }

            CommonText(
                data.subTitle ?: "",
                fontSize = 45.csp,
                maxLines = 3,
                lineHeight = 50.csp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        vertical = 35.cdp
                    )
            )

            Log.d("AAAAAAAAAAAAAAAAA: ","$checkItems")

            checkItems?.forEach { item ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(60.cdp, Alignment.Start),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item?.forEach { productAttributes ->
                        Log.d("AAAAAAAAAAAAAAAAA: ","$checkItems")

                        AdditionalItem(
                            productAttributes,
                            isCheck = productAttributes.isCheckCheckBox ?: false,
                            onCheck = {
                                Log.d("isCheckCheckBox", "click")
                                onChecked.invoke(data.id, productAttributes.id ?: -1, !(productAttributes.isCheckCheckBox ?: false))
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }

            }

            ConfirmButton(
                "立即支付",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .weight(3f)
            )
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
    attributes: ProductAttributes,
    isCheck: Boolean,
    onCheck: () -> Unit = {},
    modifier: Modifier
) {
    Log.d("DryClean::::::::" , "${attributes.toString()}")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        CommonIcon(
            resId = R.drawable.ic_green_dot,
            tint = Color.White,
            modifier = Modifier
                .size(10.cdp)
        )
        CommonText(
            attributes.name ?: "",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 15.cdp
                )
        )
        CommonText(
            "¥ ${attributes.value}",
            textAlign = TextAlign.Start,
        )
        CircleCheckbox(
            selected = isCheck,
        ) {
            onCheck.invoke()
        }
    }
}

@Preview
@Composable
fun PreView() {
    DryClean()
}
package com.qianyanhuyu.app_large.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.qianyanhuyu.app_large.ui.compose.ProductCategoryListView
import com.qianyanhuyu.app_large.ui.compose.ProductListView
import com.qianyanhuyu.app_large.ui.widgets.LoadingComponent
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.viewmodel.HotelMallViewAction
import com.qianyanhuyu.app_large.viewmodel.HotelMallViewModel

/***
 * 酒店商超
 */
@Composable
fun HotelMall(
    viewModel: HotelMallViewModel = hiltViewModel(),
    productType:Int
) {
    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.viewStates.productType=productType
        viewModel.dispatch(HotelMallViewAction.InitPageData)
        onDispose {  }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.cdp)
    ) {

        Column() {
            ProductCategoryListView(
                data = viewModel.viewStates.categorys,
                onSelected={
                    viewModel.dispatch(HotelMallViewAction.SelectedCategory(categoryId = it.id!!))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.cdp)
            )
            Spacer(modifier = Modifier.height(20.cdp))
            ProductListView(
                data =viewModel.viewStates.products ,
                modifier = Modifier
                    .fillMaxSize()
            )
        }


        if(viewModel.viewStates.isLoading)
            LoadingComponent(
                isScreen = true
            )
    }

}


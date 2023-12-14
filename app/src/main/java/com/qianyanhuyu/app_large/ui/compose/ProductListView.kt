package com.qianyanhuyu.app_large.ui.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.response.Product
import com.qianyanhuyu.app_large.data.response.ProductCategory
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp


@Composable
fun ProductListView(
    data: List<Product>,
    modifier : Modifier,
) {
    Box(
        modifier = modifier
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(20.cdp),
            horizontalArrangement = Arrangement.spacedBy(20.cdp)
        ){
            items(
                data,
                //span =
            ) {
                ProductView(
                    data = it,
                    modifier =Modifier.fillMaxSize().height(260.cdp)
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewProductListView(){
    var data= listOf<Product>(
        Product(
            id = 1,
            title = "乳品烘培",
            image = "https://img2.baidu.com/it/u=701893703,2436028016&fm=253&fmt=auto&app=138&f=PNG?w=500&h=500",
        ),
        Product(
            id = 2,
            title = "乳品烘培2",
            image = "https://img2.baidu.com/it/u=701893703,2436028016&fm=253&fmt=auto&app=138&f=PNG?w=500&h=500",
        ),
    );
    ProductListView(
        data = data,
        modifier =Modifier
            .fillMaxSize()
    )
}
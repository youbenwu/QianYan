package com.qianyanhuyu.app_large.ui.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.request.ImageRequest
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.response.Product
import com.qianyanhuyu.app_large.data.response.ProductCategory
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp


@Composable
fun ProductView(
    data: Product,
    modifier : Modifier,
) {
    Box(
        modifier = modifier
    ){

        val bgGradient = Brush.horizontalGradient(
            listOf(
                Color.Black,
                Color.Black.copy(
                    alpha = 0f
                ),
                Color.Black.copy(
                    alpha = 0f
                )
            )
        )

        Row(
            modifier= Modifier
                .fillMaxSize()
                .background(bgGradient)
                .clip(RoundedCornerShape(10.cdp))
        ) {
            ProductInfo(
                data = data,
                modifier= Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
                    .padding(20.cdp)
            )
            CommonNetworkImage(
               url = data.image,
               contentScale = ContentScale.Crop,
               modifier = Modifier
                   .fillMaxHeight()
                   .weight(0.6f)
           )
        }
    }
}

@Composable
fun ProductInfo(
    data: Product,
    modifier : Modifier,
){
    Box(modifier = modifier){
        Column (
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                text = "${data.title}",
                color = Color.White,
                fontSize = 25.csp
            )
            Text(
                text = "销量${data.sales}+",
                color = Color.Gray,
                fontSize = 18.csp
            )
            Text(
                text = "¥${data.price}",
                color = Color.White,
                fontSize = 30.csp
            )
        }
    }
}



@Preview
@Composable
fun PreviewProductView(){
    var data= Product(
        id = 1,
        title = "乳品烘培",
        image = "https://img2.baidu.com/it/u=701893703,2436028016&fm=253&fmt=auto&app=138&f=PNG?w=500&h=500",
    );
    ProductView(
        data = data,
        modifier =Modifier
            .fillMaxSize()
    )
}
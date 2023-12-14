package com.qianyanhuyu.app_large.ui.compose


import android.widget.ScrollView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.data.response.ProductCategory
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.viewmodel.HotelMallViewAction
import okhttp3.internal.wait


@Composable
fun ProductCategoryListView(
    onSelected: (ProductCategory) -> Unit,
    data: List<ProductCategory>,
    modifier : Modifier,
) {
    Box(
        modifier = modifier
    ){

        var selectedIndex by remember {
            mutableStateOf(0)
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(40.cdp),
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            items(data){
                CategoryButton(
                    category = it,
                    selected = data.indexOf(it)==selectedIndex,
                    onClick = {
                        onSelected.invoke(it)
                        selectedIndex=data.indexOf(it)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryButton(
    onClick: () -> Unit,
    category: ProductCategory,
    selected:Boolean=false,
){


    var colors:ButtonColors;
    if(selected){
        colors=ButtonDefaults.buttonColors(
            backgroundColor = Color(42,182,228).copy(1f),
            contentColor = Color.White,
        )
    }else{
        colors=ButtonDefaults.buttonColors(
            backgroundColor = Color(25,30,59,255).copy(1f),
            contentColor = Color.White,
        )
    }

    Button(
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(50.cdp)
    ) {
        Text(
            text = "${category.title}",
            fontSize = 30.csp
        )
    }
}

@Preview
@Composable
fun PreviewProductCategoryListView(){
    var data= listOf<ProductCategory>(
        ProductCategory(
            id = 1,
            title = "乳品烘培",
            children = null,
        ),
        ProductCategory(
            id = 1,
            title = "乳品烘培1",
            children = null,
        ),
        ProductCategory(
            id = 1,
            title = "乳品烘培2",
            children = null,
        ),
    );
    ProductCategoryListView(
        data = data,
        modifier =Modifier
        .fillMaxSize(),
        onSelected = {}
    )
}
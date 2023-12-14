package com.qianyanhuyu.app_large.ui.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.util.cdp


@Composable
fun HomeAdvertGridView(
    data: List<Advert>,
    modifier : Modifier,
) {
    Box(
        modifier = modifier
    ){
        Row (
            modifier =Modifier
                .fillMaxSize()
        ) {
            Column (
                modifier= Modifier.weight(1f).fillMaxSize()
            ){
                FirstAdvertView(
                    data =data.getOrNull(0),
                    modifier = Modifier
                        .fillMaxSize().weight(1f)
                )
                Spacer(modifier = Modifier.height(30.cdp))
                AdvertView(
                    data =data.getOrNull(1),
                    modifier = Modifier
                        .fillMaxSize().weight(1f)
                )
            }
            Spacer(modifier = Modifier.width(30.cdp))
            Column(
                modifier=Modifier.weight(1f).fillMaxSize()
            ) {
                AdvertView(
                    data =data.getOrNull(2),
                    modifier = Modifier
                        .fillMaxSize().weight(1f)
                )
                Spacer(modifier = Modifier.height(30.cdp))
                AdvertView(
                    data =data.getOrNull(3),
                    modifier = Modifier
                        .fillMaxSize().weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeAdvertGridView(){

    HomeAdvertGridView(data = List(1, init = {t->Advert( id=1, channelId=1, title="aa", subTitle="aa",
        image="ss",
        video="http://",
        url="http://",
        qrCode="http://",
        advertType= null)}), modifier =Modifier
        .fillMaxSize() )
}
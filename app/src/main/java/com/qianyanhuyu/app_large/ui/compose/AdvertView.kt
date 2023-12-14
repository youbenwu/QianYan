package com.qianyanhuyu.app_large.ui.compose


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.qianyanhuyu.app_large.App
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.data.model.Advert
import com.qianyanhuyu.app_large.ui.AppNavController
import com.qianyanhuyu.app_large.ui.common.Route
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.OtherAppUtil
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.util.toPx
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun FirstAdvertView(
    data: Advert?,
    modifier : Modifier,
    ){
    Box(modifier = modifier){
        AdvertView(data =data , modifier = Modifier.fillMaxSize())
        CommonIcon(
            resId = R.drawable.ic_image_top_start,
            tint = Color(121, 247, 255),
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = (-10).cdp.toPx
                    this.translationY = (-1).cdp.toPx
                }
                .width(64.cdp)
                .height(54.cdp)
                .zIndex(9f)
                .align(Alignment.TopStart)
        )
        CommonIcon(
            resId = R.drawable.ic_image_bottom_start,
            tint = Color(121, 247, 255),
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = (-2).cdp.toPx
                    this.translationY = (2).cdp.toPx
                }
                .width(66.cdp)
                .height(56.cdp)
                .zIndex(9f)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun AdvertView(
    data: Advert?,
    modifier : Modifier,
) {
    Box(
        modifier = modifier
    )
    {
        AdvertContent(data = data)
    }
}

@Composable
fun AdvertContent(
    data: Advert?,
){
    val contentRadius = 5.cdp
    val leftColorsBrush = remember {
        AppConfig.brush121_192
    }
    Box(
        modifier = Modifier
            .border(BorderStroke(contentRadius, leftColorsBrush), RoundedCornerShape(contentRadius))
            .clip(RoundedCornerShape(contentRadius))
            .fillMaxSize()
    )
    {
        val url=data?.url?:"";
        val video=data?.video?:"";
        CommonNetworkImage(
            url = data?.image ?: "",
            modifier = Modifier
                .fillMaxSize().onClick {
                    if(video.length>0) {
                        OtherAppUtil.openSystemVideo(
                            url = data?.video,
                            context = App.context
                        )
                    }else if(url.length>0){
//                        OtherAppUtil.openSystemVideo(
//                            url = data?.url,
//                            context = App.context
//                        )
                        val urlEncode = URLEncoder.encode( url, StandardCharsets.UTF_8.toString())
                        AppNavController.instance.navigate("${Route.WEB_VIEW}/${data?.title?:""}/${urlEncode}")
                    }
                }
        )

        if((data?.qrCode?:"").length>0){
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(30.cdp)
            ) {
                CommonNetworkImage(
                    url = data?.qrCode?:"",
                    modifier = Modifier
                        .fillMaxWidth(0.16f)
                        .aspectRatio(1f)
//                        .border(
//                            width = 9.cdp,
//                            color = Color.White.copy(0.7f)
//                        )
                )
            }
        }


        if(url.length>0){
            TextBackground(
                text = "点击进入",
                fontSize = 28.csp,
                textColor = Color.White,
                textHorizontalPadding = 20.cdp,
                textBackgroundBrush = AppConfig.CustomButtonBrushGreen,
                shapes = Shapes.extraLarge,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 40.cdp,
                        bottom = 20.cdp
                    )
            ) {
                val urlEncode = URLEncoder.encode( url, StandardCharsets.UTF_8.toString())
                AppNavController.instance.navigate("${Route.WEB_VIEW}/${data?.title?:""}/${urlEncode}")
            }
        }



    }
}
package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.*
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

@Composable
fun ListEmptyContent(title: String, text: String = "", onRefresh: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_result))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.heightIn(0.cdp, 300.cdp).noRippleClickable(onClick = onRefresh)
        )
        LinkText(text = title, onClick = onRefresh)
        if (text.isNotBlank()) {
            Text(text = text, fontSize = 10.csp, modifier = Modifier.padding(top = 16.cdp), lineHeight = 14.csp)
        }
    }
}

@Composable
fun LoadDataContent(text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
        LottieAnimation(
            composition,
            progress
        )
        Text(
            text,
            color = Color.White,
            fontSize = 48.csp,
            modifier = Modifier.padding(top = 18.cdp)
        )
    }
}
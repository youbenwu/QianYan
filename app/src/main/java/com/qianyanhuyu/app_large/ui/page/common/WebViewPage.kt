package com.qianyanhuyu.app_large.ui.page.common

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/***
 * @Author : Cheng
 * @CreateDate : 2023/11/1 9:38
 * @Description : 通用WebView
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewPage(
    snackHostState: SnackbarHostState? = null,
    title: String? = "",
    url: String? = ""
) {
    /*BackHandler() {

    }*/


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                val webView = WebView(context)
                webView.settings.javaScriptEnabled = true
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                webView.settings.domStorageEnabled = true
                webView.settings.loadsImagesAutomatically = true
                webView.settings.mediaPlaybackRequiresUserGesture = false

                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                webView.webViewClient = mWebViewClient
                webView.webChromeClient = mWebViewChromeClient
                webView.loadUrl(url ?: "")
                webView
            },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

private val mWebViewClient = object : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.d("webView", "加载开始")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("webViewT", "加载完成")
    }
}

private val mWebViewChromeClient = object : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        Log.d("webView", "加载：$newProgress")
        super.onProgressChanged(view, newProgress)
    }
}
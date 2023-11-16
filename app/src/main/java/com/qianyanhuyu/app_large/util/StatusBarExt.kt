package com.qianyanhuyu.app_large.util

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat

/**
 * TODO App上由于直接使用的全屏,所以沉浸式状态栏的处理可能存在问题,如果之后需要状态栏可能得做沉浸式处理
 */

/**
 * 设置为沉浸式状态栏
 */
fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

/**
 * 状态栏反色
 */
fun Activity.setAndroidNativeLightStatusBar() {
    val decor = window.decorView
    val isDark = resources.configuration.uiMode == 0x21
    if (!isDark) {
        decor.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        decor.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}

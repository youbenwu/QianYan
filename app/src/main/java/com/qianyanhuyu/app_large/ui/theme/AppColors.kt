package com.qianyanhuyu.app_large.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    statusBar: Color,
    pure: Color,
    primary: Color,
) {
    var statusBarColor: Color by mutableStateOf(statusBar)
        internal set
    var pure : Color by mutableStateOf(pure)
        internal set
    var primary: Color by mutableStateOf(primary)
        internal set
}
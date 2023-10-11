package com.qianyanhuyu.app_large

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.qianyanhuyu.app_large.ui.HomeNavHost
import com.qianyanhuyu.app_large.ui.theme.App_largeTheme
import com.qianyanhuyu.app_large.util.FixSystemBarsColor
import com.qianyanhuyu.app_large.util.setAndroidNativeLightStatusBar
import com.qianyanhuyu.app_large.util.transparentStatusBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 让应用占满全屏
        transparentStatusBar()
        setAndroidNativeLightStatusBar()

        /*lifecycleScope.launch {
            delay(100)
            setContent {
                App_largeTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        HomeNavHost()
                    }
                }
            }
        }*/
        setContent {
            App_largeTheme {
                val navController = rememberAnimatedNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colorScheme.background,
                ) {
                    HomeNavHost(
                        navController
                    ) {
                        finish()
                    }

                    FixSystemBarsColor()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exitProcess(0)
    }
}
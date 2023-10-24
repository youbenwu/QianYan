package com.qianyanhuyu.app_large

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.qianyanhuyu.app_large.ui.page.MainPage
import com.qianyanhuyu.app_large.ui.theme.App_largeTheme
import com.qianyanhuyu.app_large.util.FixSystemBarsColor
import com.qianyanhuyu.app_large.util.setAndroidNativeLightStatusBar
import com.qianyanhuyu.app_large.util.transparentStatusBar
import dagger.hilt.android.AndroidEntryPoint
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
                ) {
                    MainPage(
                        navController = navController,
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
package com.qianyanhuyu.app_large.util

import android.content.Context
import android.content.Intent

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/12 16:46
 * @Description : 跳转第三方App
 */
object OtherAppUtil {
    fun openOtherApp(
        context: Context,
        packageName: String = ""
    ) {
        val pi = context.packageManager.getPackageInfo(packageName, 0)

        val intent = Intent(Intent.ACTION_MAIN)
    }
}
package com.qianyanhuyu.app_large.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.util.Log
import android.widget.Toast

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/12 16:46
 * @Description : 跳转第三方App
 */
object OtherAppUtil {
    fun openOtherApp(
        context: Context,
        packageName: OtherPackage = OtherPackage.TENCENT
    ) {
        // 包名不是Null 的时候跳转
        try {
            /*val aa = context.packageManager
            aa?.getInstalledPackages(0)?.forEach {
                Log.d("test: ", it.applicationInfo.packageName);
                Log.d("appinfo", aa.getApplicationLabel(it.applicationInfo).toString());
            }*/


            val pi: PackageInfo = context.packageManager
                .getPackageInfo(packageName.value, 0)
            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resolveIntent.setPackage(pi.packageName)
            val apps: List<ResolveInfo> =
                context.packageManager.queryIntentActivities(resolveIntent, 0)
            val ri = apps.iterator().next()
            val packageName = ri.activityInfo.packageName
            val className = ri.activityInfo.name
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName(packageName, className)
            intent.component = cn
            context.startActivity(intent)
        } catch(e: Exception) {
            e.printStackTrace();
            Log.d("OtherApplication Error: ", "otherApplication.getPackageName()");

        }
    }

    enum class OtherPackage(
        val value: String
    ) {
        TENCENT("com.tencent.qqlive")
    }
}
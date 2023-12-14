package com.qianyanhuyu.app_large.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.qianyanhuyu.app_large.data.model.AdvertContentType
import com.qianyanhuyu.app_large.ui.widgets.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/***
 * @Author : Cheng
 * @CreateDate : 2023/10/12 16:46
 * @Description : 跳转第三方App
 */
object OtherAppUtil {
    fun openOtherApp(
        context: Context,
        packageName: OtherPackage = OtherPackage.EMPTY
    ) {
        try {
            // 不建议删除  -->  查看包名的方法, 新设备或者应用商店无法确定包名可使用这个查找
            /*val aa = context.packageManager
            aa?.getInstalledPackages(0)?.forEach {
                Log.d("test: ", it.applicationInfo.packageName);
                Log.d("appinfo", aa.getApplicationLabel(it.applicationInfo).toString());
            }*/

            // 包名不是空的时候跳转
            if(packageName.value.isEmpty())
                return

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
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val cn = ComponentName(packageName, className)
            intent.component = cn
            context.startActivity(intent)
        } catch(e: Exception) {
            e.printStackTrace()
            Log.d("OtherApplication Error: ", "package: ${packageName.value}")
            val viewModelJob = Job()
            val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        showToast("暂未支持,敬请期待")
                    }
                }
            }
        }
    }

    /**
     * 打开系统媒体的方法, 查看图片或者播放视频
     * 模拟器测试可能会播放失败,建议实机测试
     */
    fun openSystemVideo(
        url: String?,
        context: Context,
    ) {
        try {
            if (url != null && "" != url) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val extension = MimeTypeMap.getFileExtensionFromUrl(url)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val uri: Uri = Uri.parse(url)

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(uri, mimeType)
                context.startActivity(intent)
            } else {

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    public fun openUrl(
        url: String?,
        context: Context,
    ){
        val urlIntent = Intent(

            Intent.ACTION_VIEW,


            Uri.parse(url)

        )

        urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        urlIntent.addFlags(Intent.URI_INTENT_SCHEME)

        context.startActivity(urlIntent)

    }

    /**
     * 第三方包名
     * 来源: 应用宝
     */
    enum class OtherPackage(
        val value: String
    ) {
        EMPTY(""),

        /**
         * 腾讯视频
         * 测试包: tv.danmaku.bili
         */
        TENCENT("com.tencent.qqlive"),

        /**
         * 爱奇艺
         */
        AI_QI_YI("com.qiyi.video"),

        /**
         * 抖音
         */
        DOU_YIN("com.ss.android.ugc.aweme"),

        /**
         * 优酷
         */
        YOU_KU("com.youku.phone"),

        /**
         * 小红书
         */
        SMALL_HONG_SHU("com.xingin.xhs")
    }
}
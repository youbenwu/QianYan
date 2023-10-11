package com.qianyanhuyu.app_large

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.qianyanhuyu.app_large.util.datastore.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 10:34
 * @Description : App 初始化内容
 */
@HiltAndroidApp
class App : Application(){

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        // 初始化DataStore
        DataStoreUtils.init(this)
    }
}
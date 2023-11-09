package com.qianyanhuyu.app_large.util

import com.qianyanhuyu.app_large.ui.widgets.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by ssk on 2022/4/17.
 */
class TwoBackFinish() {
    companion object {
        var mExitTime: Long = 0
    }

    /**
     * 点击两次退出App, 保留这部分注释代码
     */
    /*fun execute(finish:() -> Unit) {
        when {
            *//**
             * 点击两次退出程序 有事件间隔，间隔内退出程序，否则提示
             *//*
            (System.currentTimeMillis() - mExitTime) > 1500 -> {
                val viewModelJob = Job()
                val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        withContext(Dispatchers.Main) {
                            showToast("再按一次退出程序")
                        }
                    }
                }
                mExitTime = System.currentTimeMillis()
            }
            else -> {
                finish()
            }
        }
    }*/

    /**
     * 短时间内重复点击不执行
     */
    fun execute(finish:() -> Unit) {
        when {
            /**
             * 点击两次退出程序 有事件间隔，间隔内退出程序，否则提示
             */
            (System.currentTimeMillis() - mExitTime) > 1500 -> {
                finish()
                mExitTime = System.currentTimeMillis()
            }
            else -> {

            }
        }
    }
}
package com.qianyanhuyu.app_large.ui.common.swipe

sealed class RefreshType {
    object IDLE : RefreshType()
    object PULL_TO_REFRESH : RefreshType()
    object RELEASE_TO_REFRESH : RefreshType()
    object REFRESHING : RefreshType()
    object REFRESH_SUCCESS : RefreshType()
    object REFRESH_FAIL : RefreshType()
    object LOAD_MORE_ING : RefreshType()
    object LOAD_MORE_SUCCESS : RefreshType()
    object LOAD_MORE_FAIL : RefreshType()
}

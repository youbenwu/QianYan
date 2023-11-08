package com.qianyanhuyu.app_large.model

import android.os.Parcelable
import java.io.Serializable

data class Video(
    val description: String = "",
    val sources: List<String> = emptyList(),
    val subtitle: String = "",
    val title: String = "",
    val thumb: String = ""
): Serializable

data class VideoList(
    val videos: List<Video>
): Serializable
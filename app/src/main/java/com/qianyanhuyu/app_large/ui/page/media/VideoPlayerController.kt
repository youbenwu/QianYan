package com.qianyanhuyu.app_large.ui.page.media

import kotlinx.coroutines.flow.StateFlow

interface VideoPlayerController {

    fun setSource(source: VideoPlayerSource, isLoop: Boolean = false)

    fun play()

    fun pause()

    fun playPauseToggle()

    fun quickSeekForward()

    fun quickSeekRewind()

    fun seekTo(position: Long)

    fun reset()

    val state: StateFlow<VideoPlayerState>
}
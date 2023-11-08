package com.qianyanhuyu.app_large.ui.page.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.util.UnstableApi
import com.qianyanhuyu.app_large.util.cdp

@UnstableApi
@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier
) {
    val controller = LocalVideoPlayerController.current
    val videoPlayerUiState by controller.collect()

    with(videoPlayerUiState) {
        SeekBar(
            progress = currentPosition,
            max = duration,
            enabled = controlsVisible && controlsEnabled,
            onSeek = {
                controller.previewSeekTo(it)
            },
            onSeekStopped = {
                controller.seekTo(it)
            },
            secondaryProgress = secondaryProgress,
            seekerPopup = {
                PlayerSurface(
                    modifier = Modifier
                        .height(48.cdp)
                        .width(48.cdp * videoSize.first / videoSize.second)
                        .background(Color.DarkGray)
                ) {
                    controller.previewPlayerViewAvailable(it)
                }
            },
            modifier = modifier
        )
    }
}
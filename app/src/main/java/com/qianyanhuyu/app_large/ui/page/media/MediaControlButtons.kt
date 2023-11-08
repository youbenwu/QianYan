package com.qianyanhuyu.app_large.ui.page.media

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.media3.common.util.UnstableApi
import com.qianyanhuyu.app_large.ui.page.media.util.getDurationString
import com.qianyanhuyu.app_large.util.cdp

@UnstableApi
@Composable
fun MediaControlButtons(
    modifier: Modifier = Modifier
) {
    val controller = LocalVideoPlayerController.current

    val controlsEnabled by controller.collect { controlsEnabled }

    // Dictates the direction of appear animation.
    // If controlsVisible is true, appear animation needs to be triggered.
    val controlsVisible by controller.collect { controlsVisible }

    // When controls are not visible anymore we should remove them from UI tree
    // Controls by default should always be on screen.
    // Only when disappear animation finishes, controls can be freely cleared from the tree.
    val (controlsExistOnUITree, setControlsExistOnUITree) = remember(controlsVisible) {
        mutableStateOf(true)
    }

    val appearAlpha = remember { Animatable(0f) }

    LaunchedEffect(controlsVisible) {
        appearAlpha.animateTo(
            targetValue = if (controlsVisible) 1f else 0f,
            animationSpec = tween(
                durationMillis = 250,
                easing = LinearEasing
            )
        )
        setControlsExistOnUITree(controlsVisible)
    }

    if (controlsEnabled && controlsExistOnUITree) {
        MediaControlButtonsContent(
            modifier = Modifier
                .alpha(appearAlpha.value)
                .background(Color.Black.copy(alpha = appearAlpha.value * 0.6f))
                .then(modifier)
        )
    }
}

@UnstableApi
@Composable
private fun MediaControlButtonsContent(modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    Box(modifier = modifier) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                controller.hideControls()
            })
        PositionAndDurationNumbers(modifier = Modifier.align(Alignment.BottomCenter))
        PlayPauseButton(modifier = Modifier.align(Alignment.Center))
    }
}

@UnstableApi
@Composable
fun PositionAndDurationNumbers(
    modifier: Modifier = Modifier
) {
    val controller = LocalVideoPlayerController.current

    val positionText by controller.collect {
        getDurationString(currentPosition, false)
    }
    val remainingDurationText by controller.collect {
        getDurationString(duration - currentPosition, false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.cdp)
    ) {
        Text(
            positionText,
            style = TextStyle(
                shadow = Shadow(
                    blurRadius = 8f,
                    offset = Offset(2f, 2f)
                )
            )
        )
        Box(modifier = Modifier.weight(1f))
        Text(
            remainingDurationText,
            style = TextStyle(
                shadow = Shadow(
                    blurRadius = 8f,
                    offset = Offset(2f, 2f)
                )
            )
        )
    }
}

@UnstableApi
@SuppressLint("PrivateResource")
@Composable
fun PlayPauseButton(modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    val isPlaying by controller.collect { isPlaying }
    val playbackState by controller.collect { playbackState }

    IconButton(
        onClick = { controller.playPauseToggle() },
        modifier = modifier
    ) {
        if (isPlaying) {
            ShadowedIcon(icon = ImageVector.vectorResource(id = androidx.media3.ui.R.drawable.exo_icon_pause))
        } else {
            when (playbackState) {
                PlaybackState.ENDED -> {
                    ShadowedIcon(icon = ImageVector.vectorResource(id = com.google.android.gms.cast.framework.R.drawable.quantum_ic_replay_white_24))
                }
                PlaybackState.BUFFERING -> {
                    CircularProgressIndicator()
                }
                else -> {
                    ShadowedIcon(icon = Icons.Filled.PlayArrow)
                }
            }
        }
    }
}
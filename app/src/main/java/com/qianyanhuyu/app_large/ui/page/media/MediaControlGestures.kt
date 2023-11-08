package com.qianyanhuyu.app_large.ui.page.media

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.page.media.util.getDurationString
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*
import kotlin.math.abs

@UnstableApi
@Composable
fun MediaControlGestures(
    modifier: Modifier = Modifier
) {
    val controller = LocalVideoPlayerController.current

    val controlsEnabled by controller.collect { controlsEnabled }
    val gesturesEnabled by controller.collect { gesturesEnabled }
    val controlsVisible by controller.collect { controlsVisible }
    val quickSeekDirection by controller.collect { quickSeekAction.direction }
    val draggingProgress by controller.collect { draggingProgress }

    if (controlsEnabled && !controlsVisible && gesturesEnabled) {
        Box(
            modifier = modifier
                .draggingProgressOverlay(draggingProgress)
                .quickSeekAnimation(quickSeekDirection) {
                    controller.setQuickSeekAction(QuickSeekAction.none())
                }) {
            GestureBox()
        }
    }

}

@UnstableApi
@Composable
fun GestureBox(modifier: Modifier = Modifier) {
    val controller = LocalVideoPlayerController.current

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(controller) {
            var wasPlaying = true
            var totalOffset = Offset.Zero
            var diffTime = -1f

            var duration: Long = 0
            var currentPosition: Long = 0

            // When this job completes, it seeks to desired position.
            // It gets cancelled if delay does not complete
            var seekJob: Job? = null

            fun resetState() {
                totalOffset = Offset.Zero
                controller.setDraggingProgress(null)
            }

            detectMediaPlayerGesture(
                onDoubleTap = { doubleTapPosition ->
                    when {
                        doubleTapPosition.x < size.width * 0.4f -> {
                            controller.quickSeekRewind()
                        }

                        doubleTapPosition.x > size.width * 0.6f -> {
                            controller.quickSeekForward()
                        }
                    }
                },
                onTap = {
                    controller.showControls()
                },
                onDragStart = { offset ->
                    wasPlaying = controller.currentState { it.isPlaying }
                    controller.pause()

                    currentPosition = controller.currentState { it.currentPosition }
                    duration = controller.currentState { it.duration }

                    resetState()
                },
                onDragEnd = {
                    if (wasPlaying) controller.play()
                    resetState()
                },
                onDrag = { dragAmount: Float ->
                    seekJob?.cancel()

                    totalOffset += Offset(x = dragAmount, y = 0f)

                    val diff = totalOffset.x

                    diffTime = if (duration <= 60_000) {
                        duration.toFloat() * diff / size.width.toFloat()
                    } else {
                        60_000.toFloat() * diff / size.width.toFloat()
                    }

                    var finalTime = currentPosition + diffTime
                    if (finalTime < 0) {
                        finalTime = 0f
                    } else if (finalTime > duration) {
                        finalTime = duration.toFloat()
                    }
                    diffTime = finalTime - currentPosition

                    controller.setDraggingProgress(
                        DraggingProgress(
                            finalTime = finalTime,
                            diffTime = diffTime
                        )
                    )

                    seekJob = coroutineScope.launch {
                        delay(200)
                        controller.seekTo(finalTime.toLong())
                    }
                }
            )
        }
        .then(modifier)
    )
}

suspend fun PointerInputScope.detectMediaPlayerGesture(
    onTap: (Offset) -> Unit,
    onDoubleTap: (Offset) -> Unit,
    onDragStart: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDrag: (Float) -> Unit
) {
    coroutineScope {
        launch {
            detectHorizontalDragGestures(
                onDragStart = onDragStart,
                onDragEnd = onDragEnd,
                onHorizontalDrag = { change, dragAmount ->
                    onDrag(dragAmount)
                    change.consumePositionChange()
                },
            )
        }

        launch {
            detectTapGestures(
                onTap = onTap,
                onDoubleTap = onDoubleTap
            )
        }
    }
}

@SuppressLint("PrivateResource")
fun Modifier.quickSeekAnimation(
    quickSeekDirection: QuickSeekDirection,
    onAnimationEnd: () -> Unit
) = composed {
    val alphaRewind = remember { Animatable(0f) }
    val alphaForward = remember { Animatable(0f) }

    LaunchedEffect(quickSeekDirection) {
        when (quickSeekDirection) {
            QuickSeekDirection.Rewind -> alphaRewind
            QuickSeekDirection.Forward -> alphaForward
            else -> null
        }?.let { animatable ->
            animatable.animateTo(1f)
            animatable.animateTo(0f)
            onAnimationEnd()
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            ShadowedIcon(
                ImageVector.vectorResource(id = androidx.media3.ui.R.drawable.exo_icon_rewind),
                modifier = Modifier
                    .alpha(alphaRewind.value)
                    .align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            ShadowedIcon(
                ImageVector.vectorResource(id = androidx.media3.ui.R.drawable.exo_icon_fastforward),
                modifier = Modifier
                    .alpha(alphaForward.value)
                    .align(Alignment.Center)
            )
        }
    }

    this
}

fun Modifier.draggingProgressOverlay(draggingProgress: DraggingProgress?) = composed {
    if (draggingProgress != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                draggingProgress.progressText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        blurRadius = 8f,
                        offset = Offset(2f, 2f)
                    )
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    this
}

data class DraggingProgress(
    val finalTime: Float,
    val diffTime: Float
) : Serializable {
    val progressText: String
        get() = "${getDurationString(finalTime.toLong(), false)} " +
            "[${if (diffTime < 0) "-" else "+"}${
                getDurationString(
                    abs(diffTime.toLong()),
                    false
                )
            }]"
}

enum class QuickSeekDirection {
    None,
    Rewind,
    Forward
}

data class QuickSeekAction(
    val direction: QuickSeekDirection
) : Serializable {
    // Each action is unique
    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(direction)
    }

    companion object {
        fun none() = QuickSeekAction(QuickSeekDirection.None)
        fun forward() = QuickSeekAction(QuickSeekDirection.Forward)
        fun rewind() = QuickSeekAction(QuickSeekDirection.Rewind)
    }
}
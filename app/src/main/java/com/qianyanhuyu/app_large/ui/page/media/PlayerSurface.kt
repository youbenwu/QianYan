package com.qianyanhuyu.app_large.ui.page.media

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
fun PlayerSurface(
    usePlayerController: Boolean = false,
    modifier: Modifier = Modifier,
    onPlayerViewAvailable: (PlayerView) -> Unit = {}
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = usePlayerController
                onPlayerViewAvailable(this)
                /*setKeepContentOnPlayerReset(true)*/
            }
        },
        modifier = modifier
    )
}
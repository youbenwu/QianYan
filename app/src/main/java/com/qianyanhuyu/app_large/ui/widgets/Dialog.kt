package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material.MaterialRichText
import com.qianyanhuyu.app_large.util.cdp

@Composable
fun BaseMsgDialog(
    message: List<String>,
    title: String? = null,
    confirmText: String? = null,
    dismissText: String? = null,
    cancelAble: Boolean = true,
    onRequestDismiss: () -> Unit) {

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = {
            if (cancelAble) onRequestDismiss.invoke()
        },
        title = {
            if (title != null) {
                Text(title)
            }
        },
        text = {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)) {
                message.forEach {
                    MaterialRichText {
                        Markdown(
                            it
                        )
                    }
                }
            }
        },
        confirmButton = {
            confirmText?.let {
                TextButton(onClick = onRequestDismiss) {
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }

        },
        dismissButton = {
            dismissText?.let {
                TextButton(onClick = onRequestDismiss) {
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
        },
        modifier = Modifier
            .padding(vertical = 30.cdp)
            .fillMaxWidth()
    )
}
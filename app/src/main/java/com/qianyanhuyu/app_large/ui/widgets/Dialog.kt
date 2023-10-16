package com.qianyanhuyu.app_large.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.constants.AppConfig.CustomGreen165
import com.qianyanhuyu.app_large.constants.AppConfig.CustomOrigin
import com.qianyanhuyu.app_large.constants.AppConfig.CustomYellow
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.AuthButtonTextColor
import com.qianyanhuyu.app_large.ui.theme.ButtonColor
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp

@Composable
fun BaseMsgDialog(
    title: String? = null,
    confirmText: String? = null,
    dismissText: String? = null,
    cancelAble: Boolean = true,
    onRequestDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (cancelAble) onRequestDismiss.invoke()
        },
        title = {
            if (title != null) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(title)
                    CommonLocalImage(
                        resId = R.drawable.ic_dialog_cancel
                    )
                }
            }
        },
        text = content,
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

/**
 * 使用方式: QianYanPlay.kt 主内容Box中在增加
 * MessageDialog(
 *      title = "尊敬的用户",
 *      isShow = viewModel.viewStates.isShowTripsDialog,
 *      onConfirmClick = {
 *      viewModel.dispatch(QianYanPlayViewAction.CheckIsVip(true))
 *   },
 *      onCancelClick = {
 *         viewModel.dispatch(QianYanPlayViewAction.CheckIsVip())
 *      }
 * )
 */
@Composable
fun BaseMessageDialog(
    isShow: Boolean = false,
    dialogBackground: Color = Color(27, 47, 120),
    dialogHeight: Float = 0.65f,
    content: @Composable () -> Unit
) {
    val alertDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isShow) {
        alertDialog.value = isShow
    }

    if(alertDialog.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            Card(
                backgroundColor = dialogBackground,
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(dialogHeight)
                    .clip(RoundedCornerShape(50.cdp))
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun VipCheckDialog(
    title: String = "",
    confirmText: String = "Confirm",
    isShow: Boolean = false,
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {
    BaseMessageDialog(
        isShow = isShow,
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (
                cancelView,
                titleTextView,
                titleView,
                contentView1,
                contentView2,
                contentView21,
                contentView31,
                contentView32,
                contentView33,
                contentView34,
                confirmTripsView,
                confirmView,
                bottomView,
            ) = createRefs()

            CommonLocalImage(
                resId = R.drawable.trips_dialog_top_bg,
                modifier = Modifier
                    .constrainAs(titleView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            CommonLocalImage(
                resId = R.drawable.trips_dialog_bottom_bg,
                modifier = Modifier
                    .constrainAs(bottomView) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                title,
                textAlign = TextAlign.Center,
                fontSize = 50.csp,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(titleTextView) {
                        start.linkTo(parent.start)
                        top.linkTo(titleView.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(titleView.bottom)
                        width = Dimension.fillToConstraints
                    }
            )

            CommonLocalImage(
                resId = R.drawable.ic_dialog_cancel,
                modifier = Modifier
                    .constrainAs(cancelView) {
                        end.linkTo(parent.end)
                        top.linkTo(titleView.top)
                        bottom.linkTo(titleView.bottom)
                    }
                    .size(114.cdp)
                    .clickable {
                        onCancelClick.invoke()
                    }
            )

            Text(
                "观看VIP内容需要开通会员",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(252, 229, 97).copy(alpha = 1f),
                            Color(210, 135, 45).copy(alpha = 1f)
                        ),
                        tileMode = TileMode.Mirror
                    ),
                    fontSize = 50.csp
                ),
                modifier = Modifier
                    .constrainAs(contentView1) {
                        top.linkTo(titleView.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(
                        30.cdp
                    )
            )

            Text(
                "您暂未开通会员",
                textAlign = TextAlign.Center,
                fontSize = 35.csp,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(contentView2) {
                        top.linkTo(contentView21.top)
                        bottom.linkTo(contentView21.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            CommonLocalImage(
                resId = R.drawable.ic_emojo_cry,
                modifier = Modifier
                    .constrainAs(contentView21) {
                        top.linkTo(contentView1.bottom)
                        start.linkTo(contentView2.end, 15.cdp)
                    }
                    .size(74.cdp)
                    .clickable {
                        onCancelClick.invoke()
                    }
            )

            createHorizontalChain(
                contentView31,
                contentView32,
                contentView33,
                contentView34,
                chainStyle = ChainStyle.Spread
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(contentView31) {
                        top.linkTo(contentView21.bottom, 30.cdp)
                        bottom.linkTo(confirmView.top, 30.cdp)
                        start.linkTo(parent.start)
                    }
            ){
                CommonIcon(
                    resId = R.drawable.ic_dialog_download,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.cdp)
                )
                Text(
                    "下载加速",
                    fontSize = 25.csp,
                    color = Color.White,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(contentView32) {
                        start.linkTo(contentView31.end)
                        top.linkTo(contentView31.top)
                    }
            ){
                CommonIcon(
                    resId = R.drawable.ic_dialog_download,
                    tint = CustomOrigin,
                    modifier = Modifier
                        .size(24.cdp)
                )
                Text(
                    "下载加速",
                    fontSize = 25.csp,
                    color = Color.White,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(contentView33) {
                        start.linkTo(contentView32.end)
                        top.linkTo(contentView31.top)
                    }
            ){
                CommonIcon(
                    resId = R.drawable.ic_dialog_download,
                    tint = CustomYellow,
                    modifier = Modifier
                        .size(24.cdp)
                )
                Text(
                    "下载加速",
                    fontSize = 25.csp,
                    color = Color.White,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .constrainAs(contentView34) {
                        start.linkTo(contentView33.end)
                        top.linkTo(contentView31.top)
                        end.linkTo(parent.end)
                    }
            ){
                CommonIcon(
                    resId = R.drawable.ic_dialog_download,
                    tint = CustomGreen165,
                    modifier = Modifier
                        .size(24.cdp)
                )
                Text(
                    "下载加速",
                    fontSize = 25.csp,
                    color = Color.White,
                )
            }

            TextBackground(
                text = "首月特惠",
                textBackground = AppConfig.CustomGreen,
                modifier = Modifier
                    .constrainAs(confirmTripsView) {

                    }
            )

            Button(
                onClick = onConfirmClick,
                shape = Shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor,
                    contentColor = AuthButtonTextColor
                ),
                modifier = Modifier
                    .constrainAs(confirmView) {
                        top.linkTo(contentView31.bottom)
                        bottom.linkTo(bottomView.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.6f)
            ) {
                Text(
                    text = confirmText,
                    fontSize = 30.csp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoadingDialog(show: Boolean) {
    Dialog(onDismissRequest = { show }) {
        Card(
            backgroundColor = Color.White,
            elevation = 8.dp,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                LoadingComponent()
            }
        }
    }
}
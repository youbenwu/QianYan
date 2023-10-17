package com.qianyanhuyu.app_large.ui.page.groupchat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.LogoImageText
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.util.toPx

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/17 11:55
 * @Description : 聊天室列表页面
 */
@Composable
fun GroupChats(
    onAddGroupChat: (() -> Unit)? = null,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        GroupChasContent(
            onAddGroupChat = onAddGroupChat,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun GroupChasContent(
    onAddGroupChat: (() -> Unit)? = null,
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(
                30.cdp
            )
    ) {
        val(
            topView,
            contentView,
        ) = createRefs()

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.cdp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .constrainAs(topView) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                }
                .fillMaxWidth()
        ) {
            CommonLocalImage(
                resId = R.drawable.ic_group_chats_logo,
                modifier = Modifier
                    .size(72.cdp)
            )
            CommonText(
                "精选群聊",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 50.csp,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(76, 155, 245),
                            Color(169, 252, 252)
                        ),
                        tileMode = TileMode.Mirror
                    ),
                    fontStyle = FontStyle.Italic,
                    shadow = Shadow(
                        color = Color.Blue,
                        offset = Offset(2f,5f),
                        blurRadius = 7f
                    )
                ),
                modifier = Modifier
                    .weight(1f)
            )
            CommonLocalImage(
                resId = R.drawable.ic_add_group_chat,
                modifier = Modifier
                    .size(72.cdp)
                    .onClick {
                        onAddGroupChat?.invoke()
                    }
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(contentView) {
                    top.linkTo(topView.bottom, margin = 30.cdp)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    bottom.linkTo(parent.bottom)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            GroupChatItem(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}



@Composable
private fun GroupChatItem(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(36, 48, 72),
                        Color(16, 24, 58)
                    )
                ),
                Shapes.extraSmall
            )
            .clip(Shapes.extraSmall)
    ) {
        AnimatedVisibility(
            visible = true,
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            CommonLocalImage(
                resId = R.drawable.ic_hot,
                modifier = Modifier
                    .width(62.cdp)
                    .height(40.cdp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(15.cdp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 30.cdp,
                    end = 15.cdp,
                    top = 15.cdp,
                    bottom = 15.cdp
                )
        ) {
            LogoImageText(
                text = "兴趣交流",
                iconDrawable = R.drawable.ic_group_chat_type_logo,
                iconWidth = 26.cdp,
                iconHeight = 30.cdp,
                fontSize = 24.csp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(Shapes.extraLarge)
                    .background(
                        Color(9, 14, 41).copy(alpha = 0.3f)
                    )
                    .padding(
                        vertical = 10.cdp,
                        horizontal = 20.cdp
                    )
            )
            CommonText(
                "KTV70.80听听今典老歌",
                fontSize = 25.csp,
                modifier = Modifier
                    .padding(
                        start = 20.cdp
                    )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.cdp, Alignment.End),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    listOf(
                        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
                        "https://img.js.design/assets/img/6520d7aac184a69b01838e25.png#7336e11e3000d93bbc5d91eac9543b38",
                        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
                        "https://img.js.design/assets/img/64460169ed05937640eb5146.png#a6d093d1a3b96c6b883e4d523c187606"
                    ).forEachIndexed { index, it ->
                        CommonNetworkImage(
                            url = it,
                            modifier = Modifier
                                .size(30.cdp)
                                .offset {
                                    IntOffset(
                                        -15.cdp.toPx.toInt() * index,
                                        IntOffset.Zero.y
                                    )
                                }
                                .clip(CircleShape)
                                .border(
                                    width = 2.cdp,
                                    color = Color(166, 166, 166),
                                    shape = CircleShape
                                )
                                .zIndex(-index.toFloat() + 1)
                        )
                    }
                }

                CommonText(
                    "200人在线",
                    fontSize = 20.csp,
                )
            }
        }
    }
}
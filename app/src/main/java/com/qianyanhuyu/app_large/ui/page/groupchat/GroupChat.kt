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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.model.MultiMenuItem
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.ImageCircle
import com.qianyanhuyu.app_large.ui.page.common.LogoImageText
import com.qianyanhuyu.app_large.ui.page.common.TextBackground
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonIcon
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.util.toPx

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/18 16:44
 * @Description : 聊天室
 */

@Composable
fun GroupChat(
    channelId: String,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GroupChatContent(
            onBack = onBack,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.cdp)
        )
    }

}

@Composable
private fun GroupChatContent(
    onBack: () -> Unit = {},
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        GroupChatTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.cdp,
                    start = 30.cdp,
                    end = 18.cdp
                )
        )

        GroupChatCenterContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        GroupChatBottomBar(
            menuList = listOf(
                MultiMenuItem(
                    index = 0,
                    icon = R.drawable.ic_voice,
                    label = "打开"
                ),
                MultiMenuItem(
                    index = 1,
                    icon = R.drawable.ic_settings,
                    label = "设置"
                ),
                MultiMenuItem(
                    index = 2,
                    icon = R.drawable.ic_share,
                    label = "分享"
                ),
                MultiMenuItem(
                    index = 3,
                    icon = R.drawable.ic_back,
                    label = "退出",
                    itemClick = onBack
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun GroupChatCenterContent(
    modifier: Modifier
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (
            leftView,
            rightView
        ) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(leftView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    start.linkTo(parent.start)
                    end.linkTo(rightView.start)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            TextBackground(
                text = "本平台提倡文明交流",
                fontSize = 20.csp,
                textBackground = AppConfig.CustomLogoImageTextBg,
                modifier = Modifier
                    .padding(
                        10.cdp
                    )

            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(20.cdp),
            modifier = Modifier
                .width(150.cdp)
                .constrainAs(rightView) {
                    top.linkTo(parent.top, margin = 15.cdp)
                    bottom.linkTo(parent.bottom, margin = 15.cdp)
                    end.linkTo(parent.end)

                    height = Dimension.fillToConstraints
                }
        ) {
            PersonImageItem(
                isTalk = true,
                modifier = Modifier
                    .fillMaxWidth()
            )

            PersonImageItem(
                index = "2",
                isTalk = false,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PersonImageItem(
    index: String = "1",
    isTalk: Boolean,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(15.cdp, Alignment.End),
        modifier = modifier
            .padding(
                horizontal = 18.cdp
            )
    ) {
        CommonNetworkImage(
            url = "https://img.js.design/assets/img/6520d7aac184a69b01838e25.png#7336e11e3000d93bbc5d91eac9543b38)",
            modifier = Modifier
                .size(60.cdp)
                .clip(CircleShape)
                .border(
                    width = 1.cdp,
                    color = Color(166, 166, 166),
                    shape = CircleShape
                )
        )

        ChatVoiceView(
            index = index,
            isTalk = isTalk
        )
    }
}

@Composable
private fun ChatVoiceView(
    index: String = "1",
    isShowVoice: Boolean = true,
    isOpenVoice: Boolean = false,
    isTalk: Boolean = false,
    modifier: Modifier = Modifier
) {
    val voiceBg = if(isTalk) {
        AppConfig.createGroupChatDialogBrush
    } else {
        AppConfig.voiceFalseBrush
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.cdp, Alignment.CenterVertically),
        modifier = modifier
            .width(25.cdp)
            .height(60.cdp)
            .clip(Shapes.extraLarge)
            .background(
                voiceBg,
                Shapes.extraLarge
            )
    ) {
        if(isTalk) {
            CommonIcon(
                resId = R.drawable.ic_wifi,
                tint = Color.White,
                modifier = Modifier
                    .width(22.cdp)
                    .height(25.cdp)
                    .padding(
                        bottom = 2.cdp,
                        end = 1.cdp
                    )
            )
        } else {
            CommonText(
                index,
                fontSize = 20.csp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier
                    .padding(
                        start = 2.cdp
                    )
            )
        }

        AnimatedVisibility(
            visible = isShowVoice
        ) {
            CommonIcon(
                resId = R.drawable.ic_voice,
                tint = Color.White,
                modifier = Modifier
                    .size(16.cdp)
                    .weight(1f)
                    .padding(
                        start = 2.cdp
                    )
            )
        }
    }
}

@Composable
private fun GroupChatTopBar(
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.cdp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LogoImageText(
                text = "创业思路交流分享",
                iconDrawable = R.drawable.ic_group_chat_type_logo,
                iconWidth = 26.cdp,
                iconHeight = 30.cdp,
                fontSize = 24.csp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(Shapes.extraLarge)
                    .background(
                        AppConfig.CustomLogoImageTextBg.copy(alpha = 0.3f)
                    )
                    .padding(
                        vertical = 10.cdp,
                        horizontal = 20.cdp
                    )
                    .align(Alignment.CenterStart)
            )
        }

        LogoImageText(
            text = "12人",
            iconDrawable = R.drawable.ic_chat_person,
            iconWidth = 20.cdp,
            iconHeight = 20.cdp,
            fontSize = 24.csp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clip(Shapes.extraLarge)
                .background(
                    AppConfig.CustomLogoImageTextBg.copy(alpha = 0.3f)
                )
                .padding(
                    vertical = 10.cdp,
                    horizontal = 20.cdp
                )
        )
    }
}

@Composable
private fun GroupChatBottomBar(
    menuList: List<MultiMenuItem>,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .background(Color.Black)
    ) {
        menuList.forEach {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.cdp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 25.cdp)
                    .onClick {
                        it.itemClick.invoke()
                    }
            ) {
                CommonIcon(
                    resId = it.icon,
                    tint = Color.White,
                    modifier = Modifier
                        .size(45.cdp)
                )
                CommonText(
                    it.label,
                    fontSize = 20.csp
                )
            }
        }
    }
}
package com.qianyanhuyu.app_large.ui.page.groupchat

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.itemsIndexed
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppConfig
import com.qianyanhuyu.app_large.model.GroupChatItem
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.LogoImageText
import com.qianyanhuyu.app_large.ui.page.common.paging.ViewStateListPagingComponent
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.CommonLocalImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.util.cdp
import com.qianyanhuyu.app_large.util.csp
import com.qianyanhuyu.app_large.util.onClick
import com.qianyanhuyu.app_large.util.toPx
import com.qianyanhuyu.app_large.viewmodel.groupchat.GroupChatsViewAction
import com.qianyanhuyu.app_large.viewmodel.groupchat.GroupChatsViewModel
import com.qianyanhuyu.app_large.viewmodel.groupchat.GroupChatsViewState
import kotlinx.coroutines.flow.Flow

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/17 11:55
 * @Description : 聊天室列表页面
 */
@Composable
fun GroupChats(
    onAddGroupChat: (() -> Unit)? = null,
    viewModel: GroupChatsViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val pageNav = remember { mutableStateOf(1) }
    val channelId = remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        // 初始化需要执行的内容
        viewModel.dispatch(GroupChatsViewAction.InitPageData)
        onDispose {  }
    }

    Box(
        modifier = modifier
    ) {
        when(pageNav.value) {
            1 -> GroupChasContent(
                viewState = viewModel.viewStates,
                onAddGroupChat = onAddGroupChat,
                onNavTo = {
                    channelId.value = it
                    pageNav.value = 2
                },
                modifier = Modifier
                    .fillMaxSize()
            )
            else -> {
                GroupChat(
                    channelId = channelId.value,
                    onBack = {pageNav.value = 1}
                )
            }
        }

    }
}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterialApi::class)
@Composable
private fun GroupChasContent(
    viewState: GroupChatsViewState,
    onAddGroupChat: (() -> Unit)? = null,
    onNavTo: (String) -> Unit = {},
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
                        blurRadius = 10f
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

        viewState.chatsFlow?.let {
            GroupChatList(
                data = it,
                onNavTo = onNavTo,
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
            )
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GroupChatList(
    data: Flow<PagingData<GroupChatItem>>,
    onNavTo: (String) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        ViewStateListPagingComponent(
            modifier = Modifier.fillMaxSize(),
            key = "group_chat_list",
            loadDataBlock = {
                data
            },
            enableRefresh = true,
        ) {beanList ->
            itemsIndexed(beanList) {_, data ->
                data?.let {
                    GroupChatItem(
                        groupChatItem = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onClick {
                                onNavTo.invoke(
                                    it.channelId ?: ""
                                )
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupChatItem(
    groupChatItem: GroupChatItem,
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
            visible = groupChatItem.isHot,
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
                text = groupChatItem.type,
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
            )
            CommonText(
                groupChatItem.title,
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
                    groupChatItem.onlinePersonImage.forEachIndexed { index, it ->
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
                    "${groupChatItem.onlinePerson}人在线",
                    fontSize = 20.csp,
                )
            }
        }
    }
}
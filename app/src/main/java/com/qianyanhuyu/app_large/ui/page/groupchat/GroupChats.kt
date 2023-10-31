package com.qianyanhuyu.app_large.ui.page.groupchat

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import com.qianyanhuyu.app_large.model.GroupChatEditTextType
import com.qianyanhuyu.app_large.model.GroupChatForm
import com.qianyanhuyu.app_large.model.GroupChatItem
import com.qianyanhuyu.app_large.ui.page.common.CommonText
import com.qianyanhuyu.app_large.ui.page.common.LogoImageText
import com.qianyanhuyu.app_large.ui.page.common.paging.ViewStateListPagingComponent
import com.qianyanhuyu.app_large.ui.theme.IconColor
import com.qianyanhuyu.app_large.ui.theme.Shapes
import com.qianyanhuyu.app_large.ui.widgets.ActivationClickText
import com.qianyanhuyu.app_large.ui.widgets.BaseMessageDialog
import com.qianyanhuyu.app_large.ui.widgets.CommonCheckBox
import com.qianyanhuyu.app_large.ui.widgets.CommonComposeImage
import com.qianyanhuyu.app_large.ui.widgets.CommonNetworkImage
import com.qianyanhuyu.app_large.ui.widgets.SimpleEditTextWidget
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
            1 -> Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GroupChasContent(
                    viewState = viewModel.viewStates,
                    onAddGroupChat = {
                        viewModel.dispatch(GroupChatsViewAction.IsShowCreateGroupChatDialog(!viewModel.viewStates.isShowDialog))
                    },
                    onNavTo = {
                        channelId.value = it
                        pageNav.value = 2
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )

                CreateChatGroupDialog(
                    title = "欢迎体验「语音聊天室」功能",
                    viewStates = viewModel.viewStates,
                    onTermClick = { type, isCheck ->
                        viewModel.dispatch(GroupChatsViewAction.UpdateFormValue(type, isCheck.toString()))
                    },
                    onClearClick = {
                        viewModel.dispatch(GroupChatsViewAction.UpdateFormValue(it))
                    },
                    onValueChange = { type, text ->
                        viewModel.dispatch(GroupChatsViewAction.UpdateFormValue(type, text))
                    },
                    data = viewModel.viewStates.formList,

                    ) {
                        viewModel.dispatch(GroupChatsViewAction.IsShowCreateGroupChatDialog(false))
                    }
            }
            else -> {
                GroupChat(
                    channelId = channelId.value,
                    onBack = {pageNav.value = 1}
                )
            }
        }

    }
}

@OptIn(ExperimentalTextApi::class)
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
            CommonComposeImage(
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
            CommonComposeImage(
                resId = R.drawable.ic_add_group_chat,
                modifier = Modifier
                    .size(72.cdp)
                    .onClick {
                        onAddGroupChat?.invoke()
                    }
            )
        }

        // 聊天室列表
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

/**
 * 聊天室列表
 */
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
            CommonComposeImage(
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun CreateChatGroupDialog(
    title: String,
    viewStates: GroupChatsViewState,
    onClearClick: (GroupChatEditTextType) -> Unit = {},
    onValueChange: ((type: GroupChatEditTextType, text: String) -> Unit)? = null,
    onTermClick: ((type: GroupChatEditTextType, isShow: Boolean) -> Unit)? = null,
    data: List<GroupChatForm> = listOf(),
    onCancelClick: () -> Unit = {}
) {
    BaseMessageDialog(
        isShow = viewStates.isShowDialog,
        dialogHeight = 0.75f,
        dialogBackground = Color(18, 18, 40),
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (
                cancelView,
                titleTextView,
                titleView,
                chatGroupFormView,
                confirmView,
                bottomView,
            ) = createRefs()

            CommonComposeImage(
                resId = R.drawable.trips_dialog_top_bg,
                modifier = Modifier
                    .constrainAs(titleView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            CommonComposeImage(
                resId = R.drawable.trips_dialog_bottom_bg,
                colorFilter = ColorFilter.tint(AppConfig.CustomBlue84),
                modifier = Modifier
                    .constrainAs(bottomView) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        width = Dimension.fillToConstraints
                    }
            )

            CommonText(
                title,
                fontSize = 50.csp,
                modifier = Modifier
                    .constrainAs(titleTextView) {
                        start.linkTo(parent.start)
                        top.linkTo(titleView.top)
                        end.linkTo(cancelView.start)
                        bottom.linkTo(titleView.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(
                        start = 20.cdp
                    )
            )

            CommonComposeImage(
                resId = R.drawable.ic_dialog_cancel,
                modifier = Modifier
                    .constrainAs(cancelView) {
                        end.linkTo(parent.end)
                        top.linkTo(titleView.top)
                        bottom.linkTo(titleView.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .aspectRatio(1F)
                    .clip(
                        RoundedCornerShape(
                            topEndPercent = 30.cdp.toPx.toInt(),
                        )
                    )
                    .clickable {
                        onCancelClick.invoke()
                    }
            )

            CreateChatGroupForm(
                data = data,
                termIsCheck = viewStates.termIsCheck,
                onTermClick = onTermClick,
                onClearClick = onClearClick,
                onValueChange = onValueChange,
                modifier = Modifier
                    .constrainAs(chatGroupFormView) {
                        top.linkTo(titleView.bottom)
                        bottom.linkTo(confirmView.top)
                        linkTo(start = parent.start, end = parent.end)

                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(confirmView) {
                        top.linkTo(chatGroupFormView.bottom)
                        bottom.linkTo(bottomView.top, margin = 25.cdp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clip(Shapes.extraLarge)
                    .background(
                        AppConfig.createGroupChatDialogBrush
                    )
                    .fillMaxWidth(0.35f)
                    .onClick { }
            ) {
                Text(
                    text = "立即创建",
                    fontSize = 37.csp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        brush = AppConfig.whiteToTransaction,
                    ),
                    modifier = Modifier
                        .padding(vertical = 25.cdp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateChatGroupForm(
    data: List<GroupChatForm>,
    termIsCheck: Boolean = false,
    onClearClick: (GroupChatEditTextType) -> Unit = {},
    onValueChange: ((type: GroupChatEditTextType, text: String) -> Unit)? = null,
    onTermClick: ((type: GroupChatEditTextType, isShow: Boolean) -> Unit)? = null,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(30.cdp),
        modifier = modifier
            .padding(30.cdp)
    ) {
        data.forEach {
            SimpleEditTextWidget(
                value = it.data,
                valueLabel = it.title,
                placeholder = {
                    CommonText(
                        stringResource(id = it.placeholder),
                        color = AppConfig.CustomGrey,
                        modifier = Modifier
                            .padding(vertical = 8.cdp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = AppConfig.CustomGrey.copy(alpha = 0.5F),
                    unfocusedIndicatorColor = AppConfig.CustomGrey,
                    unfocusedLeadingIconColor = IconColor,
                    focusedLeadingIconColor = IconColor,
                    unfocusedTrailingIconColor = Color.White,
                    focusedTrailingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                ),
                onClick = {
                    onClearClick.invoke(it.type)
                },
                onValueChange = { value ->
                    onValueChange?.invoke(it.type, value)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .toggleable(
                    value = termIsCheck,
                    role = Role.Checkbox,
                    onValueChange = {
                        onTermClick?.invoke(GroupChatEditTextType.GroupTerm, it)
                    }
                )
                .fillMaxWidth()
        ) {
            CommonCheckBox(
                isCheck = termIsCheck,
            )

            ActivationClickText(
                text = R.string.group_chat_form_term,
                clickText = "用户使用群聊协议",
                clickTextColor = AppConfig.CustomBlue84,
                fontSize = 25.csp,
                modifier = Modifier
                    .weight(1F)
                    .padding(start = 15.cdp)
            ) {
                Log.d("ClickText: ", "ActivationClickText")
            }
        }
    }
}
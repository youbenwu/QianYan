package com.qianyanhuyu.app_large.viewmodel.groupchat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.qianyanhuyu.app_large.R
import com.qianyanhuyu.app_large.constants.AppPagingConfig
import com.qianyanhuyu.app_large.data.ContentApi
import com.qianyanhuyu.app_large.data.model.CommonPageDataSource
import com.qianyanhuyu.app_large.model.GroupChatItem
import com.qianyanhuyu.app_large.model.GroupChatEditTextType
import com.qianyanhuyu.app_large.model.GroupChatForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/17 15:57
 * @Description : 群聊列表
 */

@HiltViewModel
class GroupChatsViewModel @Inject constructor(
    private val contentApi: ContentApi
) : ViewModel() {

    var viewStates by mutableStateOf(GroupChatsViewState())
        private set

    private val _viewEvents = Channel<GroupChatsViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private val exception = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _viewEvents.send(GroupChatsViewEvent.ShowMessage("错误："+throwable.message))
        }
    }

    fun dispatch(action: GroupChatsViewAction) {
        when (action) {
            is GroupChatsViewAction.InitPageData -> initPageData()
            is GroupChatsViewAction.IsShowCreateGroupChatDialog -> isShowCreateGroupChatDialog(action.isShowDialog)
            is GroupChatsViewAction.UpdateFormValue -> updateFormValue(action.type, action.text)
            else -> {

            }
        }
    }


    private fun initPageData() {

        viewStates = viewStates.copy(
            groupChats = listOf(
                GroupChatItem(
                    channelId = "",
                    type = "兴趣交流",
                    isHot = true,
                    title = "KTV70.80听听经典老歌",
                    onlinePerson = 200,
                    onlinePersonImage = listOf(
                        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
                        "https://img.js.design/assets/img/6520d7aac184a69b01838e25.png#7336e11e3000d93bbc5d91eac9543b38",
                        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
                        "https://img.js.design/assets/img/64460169ed05937640eb5146.png#a6d093d1a3b96c6b883e4d523c187606"
                    )
                )
            ),
            formList = listOf(
                GroupChatForm(
                    title = R.string.group_chat_form_name,
                    placeholder = R.string.group_chat_form_name_placeholder,
                    data = "",
                    type = GroupChatEditTextType.GroupName
                ),
                GroupChatForm(
                    title = R.string.group_chat_form_type,
                    placeholder = R.string.group_chat_form_type_placeholder,
                    data = "",
                    type = GroupChatEditTextType.GroupType
                )
            ),
            chatsFlow = getGroupChats().cachedIn(viewModelScope)
        )
    }

    private fun isShowCreateGroupChatDialog(isShow: Boolean) {
        viewStates = viewStates.copy(
            isShowDialog = isShow
        )
    }

    private fun updateFormValue(type: GroupChatEditTextType, data: String) {
        when(type) {
            GroupChatEditTextType.GroupTerm -> {
                viewStates = viewStates.copy(
                    termIsCheck = data.toBooleanStrict()
                )
            }
            GroupChatEditTextType.GroupType -> {
                // 在UTF-8下汉字占3个Byte
                val lengthMax = 18
                val isCheckLength = data.isNotEmpty() && data.toByteArray().size <= lengthMax

                viewStates = viewStates.copy(
                    formList = viewStates.formList.map {
                        if(it.type == type) {
                            it.copy(
                                data = if(data.isEmpty() || isCheckLength) {
                                    data
                                } else {
                                    it.data
                                }
                            )
                        } else {
                            it
                        }
                    }
                )
            }
            else -> {
                viewStates = viewStates.copy(
                    formList = viewStates.formList.map {
                        if(it.type == type) {
                            it.copy(
                                data = data
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }

    private fun getGroupChats() : Flow<PagingData<GroupChatItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = AppPagingConfig().pageSize
            ),
            pagingSourceFactory = {
                CommonPageDataSource(
                    requestCall = {
                        contentApi.getGroupChats(
                            page = it ?: 1
                        )
                    }
                )
            }
        ).flow
    }
}

data class GroupChatsViewState(
    val groupChats: List<GroupChatItem> = emptyList(),
    val chatsFlow: (Flow<PagingData<GroupChatItem>>)? = null,
    val isLogging: Boolean = true,
    val isShowDialog: Boolean = false,
    val termIsCheck: Boolean = false,
    val formList: List<GroupChatForm> = mutableListOf()
)

sealed class GroupChatsViewAction {
    object InitPageData : GroupChatsViewAction()

    data class IsShowCreateGroupChatDialog(
        val isShowDialog: Boolean = false
    ) : GroupChatsViewAction()

    data class UpdateFormValue(
        val type: GroupChatEditTextType,
        val text: String = "",
    ): GroupChatsViewAction()
}

sealed class GroupChatsViewEvent {
    data class NavTo(val route: String) : GroupChatsViewEvent()
    data class ShowMessage(val message: String) : GroupChatsViewEvent()
}
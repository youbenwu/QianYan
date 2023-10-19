package com.qianyanhuyu.app_large.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/***
 * @Author : Cheng
 * @CreateDate : 2023/10/17 16:02
 * @Description : 聊天室列表数据类
 */
data class GroupChatItem(
    @SerializedName("type")
    val type: String = "兴趣交流",
    @SerializedName("channel_id")
    val channelId: String,
    @SerializedName("is_hot")
    val isHot: Boolean = true,
    @SerializedName("title")
    val title: String = "私密讨论组",
    @SerializedName("online_person")
    val onlinePerson: Int = 0,
    @SerializedName("online_person_images")
    val onlinePersonImage: List<String> = listOf(
        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
        "https://img.js.design/assets/img/6520d7aac184a69b01838e25.png#7336e11e3000d93bbc5d91eac9543b38",
        "https://img.js.design/assets/img/6520d7c2c19c17d9efe72b05.png#eed47edb9fa79e889c2f564fa9e92c04",
        "https://img.js.design/assets/img/64460169ed05937640eb5146.png#a6d093d1a3b96c6b883e4d523c187606"
    )
) : Serializable
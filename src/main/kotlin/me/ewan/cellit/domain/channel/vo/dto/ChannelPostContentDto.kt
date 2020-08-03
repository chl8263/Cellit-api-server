package me.ewan.cellit.domain.channel.vo.dto

import java.text.SimpleDateFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ChannelPostContentDto(
        var channelPostId: Long? = null,
        var channelPostName: String = "",
        var accountId: Long? = null,
        var accountName: String = "",
        var channelPostContentId: Long? = null,
        var channelPostContent: String = "",
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
)
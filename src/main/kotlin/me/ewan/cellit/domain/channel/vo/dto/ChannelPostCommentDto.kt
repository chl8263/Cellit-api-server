package me.ewan.cellit.domain.channel.vo.dto

import java.text.SimpleDateFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ChannelPostCommentDto(
        var channelPostId: Long? = null,
        var accountId: Long? = null,
        var accountName: String = "",
        var channelPostCommentId: Long? = null,
        var channelPostComment: String = "",
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
)
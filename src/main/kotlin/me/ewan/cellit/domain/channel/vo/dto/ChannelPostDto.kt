package me.ewan.cellit.domain.channel.vo.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ChannelPostDto(

        var channelPostId: Long? = null,

        @NotEmpty
        @NotNull
        var channelPostName: String? = null,

        @NotEmpty
        @NotNull
        var channelPostContent: String? = null,

        @NotEmpty
        @NotNull
        var accountId: Long? = null,

        @NotEmpty
        @NotNull
        var accountName: String? = null
)
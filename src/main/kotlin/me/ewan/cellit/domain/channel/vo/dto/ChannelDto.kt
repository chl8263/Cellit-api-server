package me.ewan.cellit.domain.channel.vo.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ChannelDto(

        var channelId: Long? = null,

        @NotEmpty
        @NotNull
        var cellId: Long? = null,

        @NotEmpty
        @NotNull
        var channelName: String? = null
)
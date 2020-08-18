package me.ewan.cellit.domain.channel.dao

import me.ewan.cellit.domain.channel.vo.domain.ChannelPostComment

interface ChannelPostCommentDslRepository {

    fun findByChannelPostId(channelPostId: Long): List<ChannelPostComment>
}
package me.ewan.cellit.domain.channel.dao.Impl

import me.ewan.cellit.domain.channel.dao.ChannelPostCommentDslRepository
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostComment
import me.ewan.cellit.domain.channel.vo.domain.QChannelPost
import me.ewan.cellit.domain.channel.vo.domain.QChannelPostComment
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ChannelPostCommentDslRepositoryImpl : QuerydslRepositorySupport(ChannelPostComment::class.java), ChannelPostCommentDslRepository {

    override fun findByChannelPostId(channelPostId: Long): List<ChannelPostComment> {
        val channelPost = QChannelPost.channelPost
        val channelPostComment = QChannelPostComment.channelPostComment1

        return from(channelPostComment)
                .innerJoin(channelPostComment.channelPost, channelPost).fetchJoin()
                .where(channelPostComment.channelPost.channelPostId.eq(channelPostId))
                .fetch()
    }
}
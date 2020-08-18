package me.ewan.cellit.domain.channel.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostComment
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostContent
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostContentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.springframework.hateoas.RepresentationModel

class ChannelPostCommentEntityModel : RepresentationModel<ChannelPostCommentEntityModel> {

    @JsonUnwrapped
    var channelPostComment: ChannelPostComment

    var channelPostId: Long

    constructor(channelPostComment: ChannelPostComment, channelPostId: Long){
        this.channelPostComment = channelPostComment
        this.channelPostId = channelPostId
    }
}
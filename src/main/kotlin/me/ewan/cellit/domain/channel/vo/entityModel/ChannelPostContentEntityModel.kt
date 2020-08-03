package me.ewan.cellit.domain.channel.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostContent
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostContentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.springframework.hateoas.RepresentationModel

class ChannelPostContentEntityModel : RepresentationModel<ChannelPostContentEntityModel> {

    @JsonUnwrapped
    var channelPostContent: ChannelPostContentDto

    constructor(channelPostContent: ChannelPostContentDto){
        this.channelPostContent = channelPostContent
    }
}
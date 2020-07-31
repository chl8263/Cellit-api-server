package me.ewan.cellit.domain.channel.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.springframework.hateoas.RepresentationModel

class ChannelPostEntityModel : RepresentationModel<ChannelPostEntityModel> {

    @JsonUnwrapped
    var channelPost: ChannelPost

    constructor(channelPost: ChannelPost){
        this.channelPost = channelPost
    }
}
package me.ewan.cellit.domain.channel.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.springframework.hateoas.RepresentationModel

class ChannelPostEntityModel : RepresentationModel<ChannelPostEntityModel> {

    @JsonUnwrapped
    var channelPost: ChannelPostDto

    constructor(channelPost: ChannelPostDto){
        this.channelPost = channelPost
    }
}
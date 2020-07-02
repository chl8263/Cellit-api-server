package me.ewan.cellit.domain.channel.vo.entityModel

import com.fasterxml.jackson.annotation.JsonUnwrapped
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import org.springframework.hateoas.RepresentationModel

class ChannelEntityModel : RepresentationModel<ChannelEntityModel> {

    @JsonUnwrapped
    var channel: ChannelDto

    constructor(channel: ChannelDto){
        this.channel = channel
    }
}
package me.ewan.cellit.domain.channel.api

import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/channels"], produces = [MediaTypes.HAL_JSON_VALUE])
class ChannelController {

    @Autowired
    lateinit var channelService: ChannelService

    @Autowired
    lateinit var modelMapper: ModelMapper

    @PostMapping
    fun createChannel(@RequestBody channelDto: ChannelDto): ResponseEntity<Any>{

        val savedChannel = channelService.createChannel(channelDto)

        val entityModel = savedChannel.run {
            val tempChannelDto = modelMapper.map(this, ChannelDto::class.java)
            val channelEntityModel = ChannelEntityModel(tempChannelDto)
            val selfLink = linkTo(ChannelController::class.java).slash(this.channelId).withSelfRel()
            channelEntityModel.add(selfLink)
        }

        val linkBuilder = linkTo(ChannelController::class.java)
        val createUrl = linkBuilder.toUri()

        return ResponseEntity.created(createUrl).body(entityModel)
    }
}
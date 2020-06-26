package me.ewan.cellit.domain.channel.api

import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
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

    @PostMapping
    fun createChannel(@RequestBody channelDto: ChannelDto){//: ResponseEntity<Any>{

        channelService.createChannel(channelDto)

    }
}
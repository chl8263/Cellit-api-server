package me.ewan.cellit.domain.channel.service

import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.domain.Channel
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChannelService {

    @Autowired
    lateinit var channelRepository: ChannelRepository

    @Autowired
    lateinit var cellRepository: CellRepository

    fun createChannel(channelDto: ChannelDto): Channel {

        val cell = cellRepository.getOne(channelDto.cellId!!)

        val channel = Channel(channelName = channelDto.channelName!!, cell = cell)
        val savedChannel = channelRepository.save(channel)

        return savedChannel
    }
}
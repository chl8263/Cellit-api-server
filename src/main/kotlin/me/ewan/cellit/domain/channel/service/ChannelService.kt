package me.ewan.cellit.domain.channel.service

import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostRepository
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.domain.Channel
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChannelService {

    @Autowired
    lateinit var channelRepository: ChannelRepository

    @Autowired
    lateinit var channelPostRepository: ChannelPostRepository

    @Autowired
    lateinit var cellRepository: CellRepository

    fun createChannel(channelDto: ChannelDto): Channel {

        val cell = cellRepository.getOne(channelDto.cellId!!)

        val channel = Channel(channelName = channelDto.channelName!!, cell = cell)
        val savedChannel = channelRepository.save(channel)

        return savedChannel
    }

    fun getChannelWithChannelId(channelId: Long): Channel{
        return channelRepository.getOne(channelId)
    }

    fun getChannelsWithCellId(cellId: Long): List<Channel> {
        val cell = cellRepository.getOne(cellId)
        return cell.channels
    }

    fun saveChannelPost(channelPost: ChannelPost): ChannelPost {
        return channelPostRepository.save(channelPost)
    }

    fun getChannelPosts(channelId: Long, pageable: Pageable): Page<ChannelPost> {
        val foundChannel = channelRepository.getOne(channelId)
        return channelPostRepository.findByChannel(foundChannel, pageable)
    }
}
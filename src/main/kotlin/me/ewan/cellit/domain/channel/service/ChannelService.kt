/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.domain.channel.service

import com.querydsl.core.Tuple
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostCommentRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostContentRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostRepository
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.domain.Channel
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostComment
import me.ewan.cellit.domain.channel.vo.domain.ChannelPostContent
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

    @Autowired
    lateinit var channelPostCommentRepository: ChannelPostCommentRepository

    @Autowired
    lateinit var channelPostContentRepository: ChannelPostContentRepository

    fun createChannel(channelDto: ChannelDto): Channel {

        val cell = cellRepository.getOne(channelDto.cellId!!)

        val channel = Channel(channelName = channelDto.channelName!!, cell = cell)
        val savedChannel = channelRepository.save(channel)

        return savedChannel
    }

    fun saveChannel (channel: Channel) = channelRepository.save(channel)

    fun getChannelByChannelId(channelId: Long): Channel? = channelRepository.getOne(channelId)

    fun getChannelsByCellId(cellId: Long): List<Channel> {
        val cell = cellRepository.getOne(cellId)
        return cell.channels
    }

    fun saveChannelPost(channelPost: ChannelPost): ChannelPost = channelPostRepository.save(channelPost)
    fun saveChannelPostContent(channelPostContent: ChannelPostContent): ChannelPostContent = channelPostContentRepository.save(channelPostContent)

    fun saveChannelPost(channelPost: ChannelPost, channelPostContent: String): ChannelPost {

        val channelPostContent = ChannelPostContent(channelPostContent = channelPostContent, channelPost = channelPost)
        channelPost.channelPostContent = channelPostContent

        val savedChannelPostContent = channelPostContentRepository.save(channelPostContent)
        val savedChannelPost = channelPostRepository.save(channelPost)

        return savedChannelPost
    }

    fun getChannelPostById(channelPostId: Long) = channelPostRepository.getOne(channelPostId)

    fun getChannelPosts(channelId: Long, postNameToSearch: String, pageable: Pageable): Page<ChannelPost> {
        val foundChannel = channelRepository.getOne(channelId)
        //return channelPostRepository.findByChannel(foundChannel, pageable)
        return channelPostRepository.findByChannelAndChannelPostNameContaining(foundChannel, postNameToSearch, pageable)
    }

    fun getChannelPostContent(channelPost: ChannelPost): ChannelPostContent {
        val channelPostContentId = channelPost.channelPostContent?.channelPostContentId
        return channelPostContentRepository.getOne(channelPostContentId!!)
    }

    fun saveChannelPostComment(channelPostComment: ChannelPostComment): ChannelPostComment {
        return channelPostCommentRepository.save(channelPostComment)
    }

    fun getChannelPostCommentByChannelPostId(channelPostId: Long): List<ChannelPostComment> {
        val channelPostComments = channelPostCommentRepository.findByChannelPostId(channelPostId)
        return channelPostComments
    }
}
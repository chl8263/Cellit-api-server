package me.ewan.cellit.domain.channel.dao

import me.ewan.cellit.domain.channel.vo.domain.Channel
import me.ewan.cellit.domain.channel.vo.domain.ChannelPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ChannelPostRepository : JpaRepository<ChannelPost, Long> {

    fun findByChannel(channel: Channel, pageable: Pageable): Page<ChannelPost>
}
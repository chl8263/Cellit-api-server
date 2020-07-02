package me.ewan.cellit.domain.channel.dao

import me.ewan.cellit.domain.channel.vo.domain.Channel
import org.springframework.data.jpa.repository.JpaRepository

interface ChannelRepository : JpaRepository<Channel, Long> {
}
package me.ewan.cellit.domain.channel.dao

import me.ewan.cellit.domain.channel.vo.domain.ChannelPostContent
import org.springframework.data.jpa.repository.JpaRepository

interface ChannelPostContentRepository : JpaRepository<ChannelPostContent, Long>{
}
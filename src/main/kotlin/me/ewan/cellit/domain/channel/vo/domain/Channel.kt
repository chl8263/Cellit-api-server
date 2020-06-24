package me.ewan.cellit.domain.channel.vo.domain

import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class Channel (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var channelId: Long? = null,

    var channelName: String,

    @Temporal(TemporalType.TIMESTAMP)
    var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    @Temporal(TemporalType.TIMESTAMP)
    var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    var active: Int = 1
)
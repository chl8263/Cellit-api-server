package me.ewan.cellit.domain.channel.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class ChannelPost (

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var channelPostId: Long? = null,

        @Column
        var channelPostName: String,

        @ManyToOne
        @JoinColumn(name = "cellId")
        var channel: Channel,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
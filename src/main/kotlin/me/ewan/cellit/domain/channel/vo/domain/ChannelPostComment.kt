package me.ewan.cellit.domain.channel.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.codehaus.jackson.annotate.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class ChannelPostComment (

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var channelPostCommentId: Long? = null,

        @Column
        var channelPostComment: String,

        @Column
        var accountId: Long? = null,

        @ManyToOne
        @JoinColumn(name = "channelPostId")
        var channelPost: ChannelPost,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
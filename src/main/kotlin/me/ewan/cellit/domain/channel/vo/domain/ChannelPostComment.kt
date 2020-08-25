package me.ewan.cellit.domain.channel.vo.domain

import com.fasterxml.jackson.annotation.JsonIgnore
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

        @Column
        var accountName: String = "",

        @JsonIgnore // prevent infinity loop when trans JSON
        @ManyToOne
        @JoinColumn(name = "channelPostId")
        var channelPost: ChannelPost,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
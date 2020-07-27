package me.ewan.cellit.domain.channel.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.codehaus.jackson.annotate.JsonIgnore
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

        @Column
        var channelPostContent: String,

        @Column
        var accountId: Long,

        @Column
        var accountName: String,

        @ManyToOne
        @JoinColumn(name = "channelId")
        var channel: Channel,

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToMany(mappedBy = "channelPost", fetch = FetchType.LAZY)
        var channelPostComments: MutableList<ChannelPostComment> = mutableListOf(),


        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
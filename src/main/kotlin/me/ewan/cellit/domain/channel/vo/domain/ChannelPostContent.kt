package me.ewan.cellit.domain.channel.vo.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class ChannelPostContent (

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var channelPostContentId: Long? = null,

        @Column(length = 5000)
        var channelPostContent: String = "",

        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToOne(fetch = FetchType.LAZY, mappedBy = "channelPostContent", cascade = [CascadeType.ALL])
        var channelPost: ChannelPost? = null,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        var active: Int = 1
)
package me.ewan.cellit.domain.calendar.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.codehaus.jackson.annotate.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*


@Entity
class Calendar(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var calendarId: Long? = null,

        var subject: String? = null,

        var content: String? = null,

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToOne(mappedBy = "cell", fetch = FetchType.LAZY)
        var cell: Cell? = null,

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Column
        var active: Int = 1
)
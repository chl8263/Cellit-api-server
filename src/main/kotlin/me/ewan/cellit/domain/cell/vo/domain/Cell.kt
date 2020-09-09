package me.ewan.cellit.domain.cell.vo.domain

import me.ewan.cellit.domain.calendar.vo.domain.Calendar
import me.ewan.cellit.domain.channel.vo.domain.Channel
import org.codehaus.jackson.annotate.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
class Cell(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var cellId: Long? = null,

        @Column(unique = true)
        var cellName: String,

        @Column
        var cellDescription: String = "",

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY)
        var accounts: MutableList<AccountCell> = mutableListOf(),

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY)
        var channels: MutableList<Channel> = mutableListOf(),

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY)
        var cellRequests: MutableList<CellRequest> = mutableListOf(),

        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "calendarId", nullable = false)
        var calendar: Calendar? = null,

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
//{
//       override fun toString() = kotlinToString(properties = toStringProperties)
//
//       override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
//
//       override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)
//
//
//       companion object {
//              private val equalsAndHashCodeProperties = arrayOf(Cell::cellId)
//              private val toStringProperties = arrayOf(
//                      Cell::cellId,
//                      Cell::cellName
//              )
//       }
//}

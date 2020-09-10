/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

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

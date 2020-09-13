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

package me.ewan.cellit.domain.channel.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.Cell
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

/**
 * Channel domain.
 *
 * <p>
 *     Channel is one of component in Cell.
 *     Purpose of Channel is share some information with other member in Cell.
 *
 *     * Don't use data class because the hashCode, toString that automatically made from data class cause infinity problem. *
 * </p>
 * @author Ewan
 */
@Entity
class Channel (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var channelId: Long? = null,

    @Column
    var channelName: String,

    @ManyToOne
    @JoinColumn(name = "cellId")
    var cell: Cell,

    // default fetch type = LAZY
    @JsonIgnore // prevent infinity loop when trans JSON
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    var channelPosts: MutableList<ChannelPost> = mutableListOf(),

    @Temporal(TemporalType.TIMESTAMP)
    var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    @Temporal(TemporalType.TIMESTAMP)
    var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

    var active: Int = 1
)
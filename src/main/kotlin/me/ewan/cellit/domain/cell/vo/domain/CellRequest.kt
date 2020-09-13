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

import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

/**
 * CellRequest domain.
 *
 * <p>
 *     CellRequest domain is use for someone request to join specific CellUnit.
 *
 *     * Don't use data class because the hashCode, toString that automatically made from data class cause infinity problem. *
 * </p>
 * @author Ewan
 */
@Entity
class CellRequest(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var cellRequestId: Long? = null,

        @ManyToOne
        @JoinColumn(name = "cellId")
        var cell: Cell,

        var accountId: Long? = null,

        var accountName: String? = null,

        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
)
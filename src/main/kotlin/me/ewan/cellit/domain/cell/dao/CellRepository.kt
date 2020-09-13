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

package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.springframework.data.jpa.repository.JpaRepository

interface CellRepository : JpaRepository<Cell, Long>, CellDslRepository{

    /**
     * Retrieve Cells from the data store by query, returning a Cell list which satisfied with condition.
     *
     * @author Ewan
     * @param cellQuery query for search
     * @return a Cells of matching with query
     */
    override fun findCellsWithQuery(cellQuery: CellQuery): List<Cell>

    /**
     * Retrieve Cell from the data store by cellName, returning a Cell.
     *
     * @author Ewan
     * @param cellName
     * @return a Cell of matching with cellName
     */
    fun findByCellName(cellName: String): Cell?
}
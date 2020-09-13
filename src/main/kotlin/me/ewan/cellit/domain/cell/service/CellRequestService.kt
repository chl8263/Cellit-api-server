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

package me.ewan.cellit.domain.cell.service

import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CellRequestService {

    @Autowired
    private lateinit var cellRequestRepository: CellRequestRepository

    /**
     * Get CellRequest by cellRequest id.
     *
     * @author Ewan
     * @param cellRequestId
     * @return a CellRequest of matching with CellRequest id
     */
    fun getCellRequestWithId(cellRequestId: Long) = cellRequestRepository.getOne(cellRequestId)

    /**
     * Get CellRequest by cell id.
     *
     * @author Ewan
     * @param cellId
     * @return a CellRequest of matching with Cell id
     */
    fun getCellRequestsWithCell(cellId: Long): List<CellRequest> {
        val cellRequests = cellRequestRepository.findByCellIdOrderByCreateDate(cellId)
        return cellRequests
    }

    /**
     * Create CellRequest.
     *
     * @author Ewan
     * @param cellRequest
     * @return CellRequest
     */
    fun createCellRequest(cellRequest: CellRequest) = cellRequestRepository.save(cellRequest)

    /**
     * Get CellRequest by cell id, account id.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return a CellRequest of matching with Cell id, account id
     */
    fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest?{
        return cellRequestRepository.findCellRequestsWithCellIdAndAccountId(cellId, accountId)
    }

    /**
     * Delete CellRequest by cell id.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return a number of result
     */
    fun deleteCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): Long {
        return cellRequestRepository.deleteCellRequestsWithCellIdAndAccountId(cellId, accountId)
    }
}
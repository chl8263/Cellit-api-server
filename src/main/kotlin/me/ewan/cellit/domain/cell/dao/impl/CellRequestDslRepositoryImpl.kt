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

package me.ewan.cellit.domain.cell.dao.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import me.ewan.cellit.domain.cell.dao.CellRequestDslRepository
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.QCell
import me.ewan.cellit.domain.cell.vo.domain.QCellRequest
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceContext

class CellRequestDslRepositoryImpl : QuerydslRepositorySupport(CellRequest::class.java), CellRequestDslRepository {

//    @PersistenceContext
//    private lateinit var em: EntityManager

    override fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest? {

        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell

        return from(cellRequest)
                .where(cell.cellId.eq(cellId)
                        .and(cellRequest.accountId.eq(accountId)))
                .fetchOne()

        //val qf = JPAQueryFactory(em)

        //
//        return qf.selectFrom(cellRequest)
//                .where(cell.cellId.eq(cellId)
//                        .and(cellRequest.accountId.eq(accountId)))
//                .fetchOne()
    }

    override fun findByCellIdOrderByCreateDate(cellId: Long): List<CellRequest> {
        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell

        return from(cellRequest)
                .where(cell.cellId.eq(cellId))
                .orderBy(cellRequest.createDate.desc())
                .fetch()
    }

    @Transactional
    override fun deleteCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): Long {
        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell

        return delete(cellRequest)
                .where(cell.cellId.eq(cellId)
                        .and(cellRequest.accountId.eq(accountId)))
                .execute()
    }
}
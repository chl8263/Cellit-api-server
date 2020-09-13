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

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.QAccount
import me.ewan.cellit.domain.cell.dao.CellDslRepository
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.QAccountCell
import me.ewan.cellit.domain.cell.vo.domain.QCell
import me.ewan.cellit.domain.cell.vo.domain.QCellRequest
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Ewan
 */
class CellDslRepositoryImpl() : QuerydslRepositorySupport(Cell::class.java), CellDslRepository {

    /**
     * Get cell list with dynamic retrieve query.
     *
     * @author Ewan
     * @param cellQuery query for retrieve
     * @return
     */
    override fun findCellsWithQuery(cellQuery: CellQuery): List<Cell> {
        val cell = QCell.cell
        val query = from(cell)
        val builder = BooleanBuilder()
        if(cellQuery.cellId != null){
            builder.and(cell.cellId.eq(cellQuery.cellId))
        }
        if(cellQuery.cellName != null){
            builder.and(cell.cellName.contains(cellQuery.cellName))
        }
        query.where(builder
                .and(cell.active.eq(1)))

        return query.fetch()
    }

    /**
     * Get specific account in specific cell.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return
     */
    override fun findAccountInCell(cellId: Long, accountId: Long): Account? {
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell

        return from(account)
                .innerJoin(account.accountCells, accountCell).fetchJoin()
                .where(accountCell.cell.cellId.eq(cellId)
                        .and(account.accountId.eq(accountId)))
                .fetchOne()
    }

    /**
     * Get account list in specific cell.
     *
     * @author Ewan
     * @param cellId
     * @return
     */
    override fun findAccounts(cellId: Long): List<Account> {
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell

        return from(account)
                .innerJoin(account.accountCells, accountCell).fetchJoin()
                .where(accountCell.cell.cellId.eq(cellId))
                .fetch()
    }

    /**
     * Delete specific account in specific cell.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return
     */
    @Transactional
    override fun deleteAccount(cellId: Long, accountId: Long): Long {
        val accountCell = QAccountCell.accountCell

        return delete(accountCell)
                .where(accountCell.cell.cellId.eq(cellId)
                        .and(accountCell.account.accountId.eq(accountId)))
                .execute()
    }
}
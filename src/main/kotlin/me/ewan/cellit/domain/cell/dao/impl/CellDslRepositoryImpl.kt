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
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class CellDslRepositoryImpl() : QuerydslRepositorySupport(Cell::class.java), CellDslRepository {

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

    override fun findAccountInCell(cellId: Long, accountId: Long): Account? {
        val cell = QCell.cell
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell

        return from(account)
                .innerJoin(account.accountCells, accountCell).fetchJoin()
                .where(accountCell.cell.cellId.eq(cellId)
                        .and(account.accountId.eq(accountId)))
                .fetchOne()
    }

    override fun findAccounts(cellId: Long): List<Account> {
        val cell = QCell.cell
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell

        return from(account)
                .innerJoin(account.accountCells, accountCell).fetchJoin()
                .where(accountCell.cell.cellId.eq(cellId))
                .fetch()
    }
}
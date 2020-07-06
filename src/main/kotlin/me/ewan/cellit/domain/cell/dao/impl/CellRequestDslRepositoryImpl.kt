package me.ewan.cellit.domain.cell.dao.impl

import me.ewan.cellit.domain.cell.dao.CellRequestDslRepository
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.QCell
import me.ewan.cellit.domain.cell.vo.domain.QCellRequest
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CellRequestDslRepositoryImpl : QuerydslRepositorySupport(CellRequest::class.java), CellRequestDslRepository {

    override fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest {
        val cellRequest = QCellRequest.cellRequest
        val cell = QCell.cell
        return from(cellRequest)
                .where(cell.cellId.eq(cellId)
                        .and(cellRequest.accountId.eq(accountId)))
                .fetchOne()
    }
}
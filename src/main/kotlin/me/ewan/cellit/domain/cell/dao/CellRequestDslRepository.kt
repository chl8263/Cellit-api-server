package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.query.CellQuery

interface CellRequestDslRepository {
    fun findCellRequestsWithCellIdAndAccountId(cellId: Long, accountId: Long): CellRequest?
}
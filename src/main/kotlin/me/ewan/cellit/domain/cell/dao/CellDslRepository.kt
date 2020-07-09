package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.query.CellQuery

interface CellDslRepository {
    fun findCellsWithQuery(cellQuery: CellQuery): List<Cell>
    fun findAccountInCell(cellId: Long, accountId: Long): Account?
}
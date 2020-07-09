package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.springframework.data.jpa.repository.JpaRepository

interface CellRepository : JpaRepository<Cell, Long>, CellDslRepository{
    override fun findCellsWithQuery(cellQuery: CellQuery): List<Cell>
}
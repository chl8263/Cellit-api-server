package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import org.springframework.data.jpa.repository.JpaRepository

interface CellRequestRepository : JpaRepository<CellRequest, Long>{
}
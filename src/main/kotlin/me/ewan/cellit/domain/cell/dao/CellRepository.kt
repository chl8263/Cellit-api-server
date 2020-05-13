package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.cell.domain.Cell
import org.springframework.data.jpa.repository.JpaRepository

interface CellRepository : JpaRepository<Cell, Long> {
}
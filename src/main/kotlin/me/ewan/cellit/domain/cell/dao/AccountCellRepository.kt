package me.ewan.cellit.domain.cell.dao

import me.ewan.cellit.domain.cell.domain.AccountCell
import org.springframework.data.jpa.repository.JpaRepository

interface AccountCellRepository : JpaRepository<AccountCell, Long> {
}
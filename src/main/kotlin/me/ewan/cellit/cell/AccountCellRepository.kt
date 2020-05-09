package me.ewan.cellit.cell

import org.springframework.data.jpa.repository.JpaRepository

interface AccountCellRepository : JpaRepository<AccountCell, Long> {
}
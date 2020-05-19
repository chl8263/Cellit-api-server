package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.domain.Cell
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByAccountname(username: String?): Account
    fun findByAccountId(accountId: Long): Account
}
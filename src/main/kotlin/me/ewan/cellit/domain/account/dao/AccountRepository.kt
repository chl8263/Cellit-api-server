package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.Cell
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<Account, Long>, AccountDslRepository{
    fun findByAccountname(username: String?): Account
    fun findByAccountId(accountId: Long): Account
}
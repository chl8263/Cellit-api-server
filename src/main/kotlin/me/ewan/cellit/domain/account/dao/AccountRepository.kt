package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.domain.Cell
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<Account, Long>, AccountDslRepository{
    fun findByAccountname(username: String?): Account
    fun findByAccountId(accountId: Long): Account

    @Query("SELECT a FROM Account a  JOIN FETCH a.accountCells WHERE a.accountId = :accountId")
    fun findByAccountIdFetch(accountId: Long): Account
}
package me.ewan.cellit.domain.account.dao.impl

import me.ewan.cellit.domain.account.dao.AccountDslRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.QAccount
import me.ewan.cellit.domain.cell.vo.domain.QAccountCell
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AccountDslRepositoryImpl() : QuerydslRepositorySupport(Account::class.java), AccountDslRepository{

    override fun findAccountFetch(accountId: Long): Account {
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell
        // @Query("SELECT a FROM Account a  JOIN FETCH a.accountCells WHERE a.accountId = :accountId")
        return from(account)
                .leftJoin(account.accountCells, accountCell).fetchJoin()
                .where(account.accountId.eq(accountId))
                .fetchOne()
    }
}
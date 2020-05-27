package me.ewan.cellit.domain.account.dao.impl

import me.ewan.cellit.domain.account.dao.AccountDslRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.QAccount
import me.ewan.cellit.domain.cell.domain.QAccountCell
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AccountDslRepositoryImpl() : QuerydslRepositorySupport(Account::class.java), AccountDslRepository{
    private val account = QAccount.account
    private val accountCell = QAccountCell.accountCell

    override fun findAllAccountData(): Account =
            from(account)
                    .innerJoin(account.accountCells, accountCell)
                    .fetchOne()
}
package me.ewan.cellit.domain.account.service

import me.ewan.cellit.domain.account.dao.AccountNotificationRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.query.AccountNotificationQuery
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.global.security.AccountContext
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService : UserDetailsService {

    companion object : KLogging()

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountNotificationRepository: AccountNotificationRepository

    @Autowired
    private lateinit var passWordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        val account = accountRepository.findByAccountname(username)
        return getAccountContext(account!!)
    }

    private fun getAccountContext(account: Account): AccountContext = AccountContext.fromAccountModel(account)

    fun createAccount(account: Account) : Account {
        account.password = passWordEncoder.encode(account.password)
        val savedAccount = accountRepository.save(account)
        return savedAccount
    }

    fun getAccountWithId(accountId: Long): Account? = accountRepository.findByAccountId(accountId)
    fun getAccountWithName(username: String): Account? = accountRepository.findByAccountname(username)
//        return accountRepository.findByAccountname(username)?: throw NoSuchElementException("Cannot find this account.")
//    }

    fun getAccountCellsWithAccountId (accountId: Long): List<AccountCell>? {
        val account = accountRepository.findAccountFetch(accountId)

        return account?.accountCells
    }

    fun createAccountNotification(accountNotification: AccountNotification): AccountNotification {
        val accountNotification = accountNotificationRepository.save(accountNotification)
        return accountNotification
    }

    fun getAccountNotificationWithQuery(accountId: Long, convertedQuery: AccountNotificationQuery): List<AccountNotification> {
        val accountNotifications = accountNotificationRepository.findAccountNotificationWithQuery(accountId, convertedQuery)
        return accountNotifications
    }

}
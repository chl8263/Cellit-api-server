package me.ewan.cellit.domain.account.service

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.vo.domain.AccountRole
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
    private lateinit var passWordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        val account = accountRepository.findByAccountname(username)
        //TODO NoSuchElementException

        return getAccountContext(account)
    }

    private fun getAccountContext(account: Account): AccountContext = AccountContext.fromAccountModel(account)

    fun createAccount(account: Account) : Account {
        account.password = passWordEncoder.encode(account.password)
        val savedAccount = accountRepository.save(account)
        return savedAccount
    }

    fun getAccount(accountId: Long): Account = accountRepository.findByAccountId(accountId)
    fun getAccount(username: String): Account = accountRepository.findByAccountname(username)

    fun getAccountCellsWithAccountId (accountId: Long): List<AccountCell> {
        val account = accountRepository.findAccountFetch(accountId)


        return account.accountCells
    }
}
/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

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

    fun getAccountById(accountId: Long): Account? = accountRepository.findByAccountId(accountId)
    fun getAccountByName(username: String): Account? = accountRepository.findByAccountname(username)
//        return accountRepository.findByAccountname(username)?: throw NoSuchElementException("Cannot find this account.")
//    }

    fun getAccountCellsByAccountId (accountId: Long): List<AccountCell>? {
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
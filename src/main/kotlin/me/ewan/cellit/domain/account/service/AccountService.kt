package me.ewan.cellit.domain.account.service

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.dao.AccountRepository
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService : UserDetailsService {

    companion object : KLogging()

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passWordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        val account = accountRepository.findByUsername(username)

        return User.builder()
                .username(account.username)
                .password(account.password)
                .roles(account.role)
                .build()

    }

    fun createAccount(account: Account) : Account {
        account.password = passWordEncoder.encode(account.password)
        account.role = "USER"
        val savedAccount = accountRepository.save(account)
        return savedAccount
    }

}
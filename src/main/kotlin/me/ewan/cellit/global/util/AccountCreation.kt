package me.ewan.cellit.global.util

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class AccountCreation : ApplicationRunner {

    @Autowired
    lateinit var accountService: AccountService

    override fun run(args: ApplicationArguments?) {
        accountService.createAccount(Account(username = "ewan", password = "123"))
    }
}
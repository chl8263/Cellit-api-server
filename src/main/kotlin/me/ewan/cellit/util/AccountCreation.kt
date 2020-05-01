package me.ewan.cellit.util

import me.ewan.cellit.account.Account
import me.ewan.cellit.account.AccountService
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
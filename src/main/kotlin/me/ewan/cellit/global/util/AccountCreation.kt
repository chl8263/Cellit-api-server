package me.ewan.cellit.global.util

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.global.common.AppProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

// TODO : Should remove this test Runner
@Component
class AccountCreation : ApplicationRunner {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var appProperties: AppProperties

    override fun run(args: ApplicationArguments?) {


        accountService.createAccount(Account(accountname = appProperties.userAccountName, password = appProperties.userPassword))
        accountService.createAccount(Account(accountname = appProperties.userAccountName2, password = appProperties.userPassword2))
        accountService.createAccount(Account(accountname = appProperties.adminAccountname, password = appProperties.adminPassword, role = AccountRole.ROLE_ADMIN))
    }
}
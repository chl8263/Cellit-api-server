package me.ewan.cellit.global.util

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.global.common.AppProperties
import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.DefaultListableBeanFactory
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


        println(appProperties.clientId)
        //accountService.createAccount(Account(username = appProperties.testUserUsername, password = appProperties.testUserPassword))
        accountService.createAccount(Account(accountname = appProperties.userAccountName, password = appProperties.userPassword))
    }
}
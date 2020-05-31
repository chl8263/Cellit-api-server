package me.ewan.cellit.jwt

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import me.ewan.cellit.global.security.AccountContext
import me.ewan.cellit.global.security.JwtFactory
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

class JetFactoryTest : BaseControllerTest(){

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    private lateinit var factory: JwtFactory

    @Test
    fun `jwt generate`(){
        //Given
        val name = "test_ewan"
        val pw = "123"
        val account = Account(accountname = name,  password = pw)
        val accountContext = AccountContext.fromAccountModel(account)

        //When & Then
        logger.info { factory.generateToken(accountContext) }
    }

    private fun newAccount(name: String, pw: String) : Account {

        val savedAccount = accountService.createAccount(Account(accountname = name, password = pw))

        return savedAccount;
    }

}
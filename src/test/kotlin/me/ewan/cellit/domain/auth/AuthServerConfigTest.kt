package me.ewan.cellit.domain.auth

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import me.ewan.cellit.global.common.AppProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

class AuthServerConfigTest : BaseControllerTest() {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var appProperties: AppProperties

    @Test
    fun getAuthToken(){
        //Given
        val username = appProperties.testUserUsername
        val password = appProperties.testUserPassword
        val savedAccount = createAccount(name = username, pw = password)

        val clientID = appProperties.clientId
        val clientSecret = appProperties.clientSecret

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientID, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        )
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("access_token").exists())
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.USER): Account {
        var account = Account(username = name, password = pw, role = role)

        var savedAccount =  accountService.createAccount(account)
        return savedAccount
    }
}
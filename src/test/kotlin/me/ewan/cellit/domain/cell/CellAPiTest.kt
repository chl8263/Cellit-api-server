package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import me.ewan.cellit.global.common.AppProperties
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.MockMvcConfigurer
import org.springframework.web.context.WebApplicationContext


class CellAPiTest : BaseControllerTest() {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    private lateinit var appProperties: AppProperties

    @BeforeEach
    fun deleteRepository(){
        //TODO : Must clean repository data for each test
    }

    @Test
    fun getCellsFromAccountId(){

        //given
        val name = "test_ewan"
        val pw = "123"
        val savedAccount = createAccount(name = name, pw = pw)

        mockMvc.perform(get("/cells/1").with(user("ewan").password("123").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    fun testPostCell(){

        //when
        mockMvc.perform(post("/api/cells/1").with(user("ewan").password("123").roles("USER"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        )
                .andDo(print())
                .andExpect(authenticated())
    }

    private fun getAccessToken(): String {
        //Given
        val username = appProperties.testUserUsername
        val password = appProperties.testUserPassword
        val savedAccount = createAccount(name = username, pw = password)

        val clientID = appProperties.clientId
        val clientSecret = appProperties.clientSecret

        val perform: ResultActions = this.mockMvc.perform(post("/oauth/token")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(clientID, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"))

        val response: MockHttpServletResponse = perform.andReturn().response
        val resultString = response.contentAsString

        val parser = Jackson2JsonParser()
        return parser.parseMap(resultString)["access_token"].toString()
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.USER): Account {
        var account = Account(username = name, password = pw, role = role)

        var savedAccount =  accountService.createAccount(account)
        return savedAccount
    }
}
package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import me.ewan.cellit.global.common.AppProperties
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


class CellAPiTest : BaseControllerTest() {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    private lateinit var appProperties: AppProperties

    @BeforeEach
    fun setUp(){
        //TODO : Must clean repository data for each test

//        println("a<><><>><><><><><><><><><><><><>")
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply<DefaultMockMvcBuilder>(springSecurity())
//                .build()
    }

    @Test
    fun getCellsFromAccountId(){

        //given
        val name = appProperties.testUserUsername
        val pw = appProperties.testUserPassword
        //val savedAccount = createAccount(name = name, pw = pw)

        mockMvc.perform(get("/api/cells/1").with(user(name).password(pw).roles("USER"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        )
                .andDo(print())
                .andExpect(status().isOk)
    }


    @Test
    //@WithUserDetails("test_ewan_user")
    @WithMockUser("ewan")
    fun testPostCell(){

        val name = "ewan"
        val pw = "123"
        //val savedAccount = createAccount(name = name, pw = pw)

        //when
        //mockMvc.perform(post("/api/cells/a").with(user("ewan").password("123").roles("USER"))
        mockMvc.perform(post("/api/cells/a")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .with(csrf())
        )
                .andDo(print())
                .andExpect(jsonPath("test").exists());
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
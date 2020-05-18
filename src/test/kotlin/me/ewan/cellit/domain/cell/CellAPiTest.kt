package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.dao.AccountRepository
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

/**
 * Test class for the {@link CellController}
 *
 * @author Ewan
 */
class CellAPiTest : BaseControllerTest() {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var appProperties: AppProperties

    @BeforeEach
    fun setUp(){
        accountRepository.deleteAll()

        val username = appProperties.testUserUsername
        val password = appProperties.testUserPassword
        createAccount(name = username, pw = password)
    }

    @Test
    fun `get cells list with account id`(){

        //given
        val name = appProperties.testUserUsername
        val pw = appProperties.testUserPassword

        mockMvc.perform(get("/api/cells/1").with(user(name).password(pw).roles("USER"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        )
                .andDo(print())
                .andExpect(status().isCreated) // 201 created
                .andExpect(jsonPath("_link.self").exists())
                .andExpect(jsonPath("_link.query-events").exists())
                .andExpect(jsonPath("_link.update-events").exists())
    }

    @Test
    @WithMockUser("test_ewan_user")
    fun `test Post cell`(){

        //when
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
        //val savedAccount = createAccount(name = username, pw = password)

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
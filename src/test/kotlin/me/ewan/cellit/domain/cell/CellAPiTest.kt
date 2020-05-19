package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.domain.Cell
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
    private lateinit var cellRepository: CellRepository

    @Autowired
    private lateinit var accountCellRepository: AccountCellRepository

    @BeforeEach
    fun setUp() {
        accountRepository.deleteAll()
    }

    @Test
    @WithMockUser("test_ewan_user")
    fun `get cells list with account id`() {

        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedAccount = createAccount(name = name, pw = pw)

        val cellName = "IT team"
        val savedCell = createCell(cellName)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell)
        val savedAccountCell = accountCellRepository.save(accountCell)

//        val reCallAccount = accountRepository.findByAccountname(name)
//
        //println("/api/account/${savedAccount.accountId}/cells")

        //when
        mockMvc.perform(get("/api/account/${savedAccount.accountname}/cells"))
        //mockMvc.perform(get("/api/account/1/cells"))
                .andDo(print())

        //then
                .andExpect(status().isCreated) // 201 created
                .andExpect(jsonPath("_link.self").exists())
                .andExpect(jsonPath("_link.query-events").exists())
                .andExpect(jsonPath("_link.update-events").exists())
    }

    @Test
    @WithMockUser("test_ewan_user")
    fun `test Post cell`() {
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        //when
        mockMvc.perform(post("/api/cells/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .with(csrf())
        )
                .andDo(print())
                .andExpect(jsonPath("test").exists());
    }

    private fun getAccessToken(): String {
        //Given
        val username = appProperties.testUserAccountname
        val password = appProperties.testUserPassword
        //createAccount(name = username, pw = password)

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
        var account = Account(accountname = name, password = pw, role = role)

        var savedAccount = accountService.createAccount(account)
        return savedAccount
    }

    private fun createCell(name: String): Cell {
        var cell = Cell(cellName = name)

        var savedCell = cellRepository.save(cell)
        return savedCell
    }
}
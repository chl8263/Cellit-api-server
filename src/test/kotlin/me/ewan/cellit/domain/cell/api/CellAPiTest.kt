package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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
        cellRepository.deleteAll()
        accountCellRepository.deleteAll()
    }

    @Test
    @WithMockUser("test_ewan_user")
    fun `Create cell`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)

        //when
        mockMvc.perform(post("/api/cells")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(cellDto))
        )

        //then
                .andDo(print())
                .andExpect(jsonPath("cellId").exists())
                .andExpect(jsonPath("cellName").exists())
                .andExpect(jsonPath("_links.self").exists())

    }

    private fun getJwtToken(username: String, pw: String): String{
        val authenticationDto = JwtAuthenticationDto(username,pw)

        //when
        val perform: ResultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationDto))
        )

        val response: MockHttpServletResponse = perform.andReturn().response
        val resultString = response.contentAsString

        val parser = Jackson2JsonParser()
        return parser.parseMap(resultString)["token"].toString()
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.ROLE_USER): Account {
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
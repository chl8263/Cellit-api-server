package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

class AccountApiTest : BaseControllerTest() {

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
    fun `Create new user`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val account = AccountDto(accountname = name, password = pw)

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(account))
        )

        //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(jsonPath("accountId").exists())
                .andExpect(jsonPath("accountName").value(name))
                .andExpect(jsonPath("role").value(AccountRole.ROLE_USER.name))
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Create new user with wrong value and return Bad Request`(){
        //given
        val name = "ewan_test_test"
        val pw = "123123"

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(
                        """
                            {
                                "accountname": "$name",
                                "password": "$pw",
                                "role": "admin"
                            }
                        """.trimIndent()
                )
        )

                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `Create new user with empty value and return Bad Request`(){
        //given
        val name = "   "
        val pw = "   "
        val account = AccountDto(accountname = name, password = pw)

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(account))
        )

                //then
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `Get cells list with account id`() {

        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedAccount = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "IT team"
        val savedCell = createCell(cellName)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell, cellRole = CellRole.CREATOR)
        accountCellRepository.save(accountCell)

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/${savedAccount.accountId}/cells")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //then
                .andDo(print())
                .andExpect(status().isOk) // 201 created
                .andExpect(jsonPath("_links.self").exists())
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
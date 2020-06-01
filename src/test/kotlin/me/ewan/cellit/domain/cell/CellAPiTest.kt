package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.domain.Cell
import me.ewan.cellit.domain.cell.model.CellDto
import me.ewan.cellit.common.BaseControllerTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
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
    fun `create cell`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val cellName = "Accounting"
        val cell = CellDto(cellName = cellName)

        //when
        mockMvc.perform(post("/api/cells")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")// + getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(cell))
                .with(csrf())
        )

        //then
                .andDo(print())
                .andExpect(jsonPath("test").exists())
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " )//+ getAccessToken())
                .with(csrf())
        )
                .andDo(print())
                .andExpect(jsonPath("test").exists())
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
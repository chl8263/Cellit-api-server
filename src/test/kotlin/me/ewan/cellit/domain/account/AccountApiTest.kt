package me.ewan.cellit.domain.account

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.domain.Cell
import me.ewan.cellit.common.BaseControllerTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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

        println("/api/account/${savedAccount.accountId}/cells")

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/account/${savedAccount.accountId}/cells"))

                //then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk) // 201 created
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self").exists())
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
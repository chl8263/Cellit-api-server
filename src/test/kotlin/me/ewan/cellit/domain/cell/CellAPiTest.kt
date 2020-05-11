package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CellAPiTest : BaseControllerTest() {

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun getCellsFromAccountId(){

        //given
        val name = "test_ewan"
        val pw = "123"
        val savedAccount = createAccount(name = name, pw = pw)

        //when
        mockMvc.perform(formLogin().user(name).password(pw))
                .andDo(print())
                .andExpect(authenticated())

        mockMvc.perform(get("/cells/1"))
                .andDo(print())
                .andExpect(status().isOk)
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.USER): Account {
        var account = Account(username = name, password = pw, role = role)

        var savedAccount =  accountService.createAccount(account)
        return savedAccount
    }
}
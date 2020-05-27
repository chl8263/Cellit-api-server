package me.ewan.cellit.domain.account

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.common.BaseControllerTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated

class LoginAndOutTest : BaseControllerTest() {

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun loginSuccess(){
        //Given
        val name = "test_ewan"
        val pw = "123"
        val account = newAccount(name, pw)

        //When & Then
        mockMvc.perform(formLogin().user(account.accountname).password(pw))
                .andDo(print())
                .andExpect(authenticated())
    }

    @Test
    fun loginFail(){
        //Given
        val name = "ewan2"
        val pw = "1234"
        val account = newAccount(name, pw)

        //When & Then
        mockMvc.perform(formLogin().user(account.accountname).password("123"))
                .andDo(print())
                .andExpect(unauthenticated())
    }

    private fun newAccount(name: String, pw: String) : Account {

        val savedAccount = accountService.createAccount(Account(accountname = name, password = pw))

        return savedAccount;
    }
}
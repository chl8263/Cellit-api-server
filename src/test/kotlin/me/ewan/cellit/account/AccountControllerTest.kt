package me.ewan.cellit.account

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun loginSuccess(){
        //Given
        val name = "ewan2"
        val pw = "123"
        val account = newAccount(name, pw)

        //When & Then
        mockMvc.perform(formLogin().user(account.username).password(account.password))
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
        mockMvc.perform(formLogin().user(account.username).password("123"))
                .andDo(print())
                .andExpect(unauthenticated())
    }

    private fun newAccount(name: String, pw: String) : Account{

        val savedAccount = accountService.createAccount(Account(username = name, password = pw))

        return savedAccount;
    }
}
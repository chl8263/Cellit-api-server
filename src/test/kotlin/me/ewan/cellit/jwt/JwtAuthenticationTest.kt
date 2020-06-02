package me.ewan.cellit.jwt

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.global.security.JwtDecoder
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*

class JwtAuthenticationTest : BaseControllerTest() {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var jwtDecoder: JwtDecoder

    @Test
    fun `Get authentication token success with role user`() {
        //given
        val username = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val authenticationDto = JwtAuthenticationDto(username,pw)
        createAccount(name = username, pw = pw)

        //when
        val perform: ResultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationDto))
        )

        val response: MockHttpServletResponse = perform.andReturn().response
        val resultString = response.contentAsString

        val parser = Jackson2JsonParser()
        val token = parser.parseMap(resultString)["token"].toString()

        val extractAccountContext = jwtDecoder.decodeJwt(token)
        val tokenUsername = extractAccountContext.username
        val tokenRole = ArrayList(extractAccountContext.authorities)

        //then
        assertThat(tokenUsername).isEqualTo(username)
        assertThat(tokenRole[0].toString()).isEqualTo(AccountRole.ROLE_USER.toString())
    }

    @Test
    fun `Get authentication token success with role admin`() {
        //given
        val username = appProperties.testAdminAccountname
        val pw = appProperties.testAdminPassword
        val authenticationDto = JwtAuthenticationDto(username,pw)
        createAccount(name = username, pw = pw, role = AccountRole.ROLE_ADMIN)

        //when
        val perform: ResultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(authenticationDto))
        )

        val response: MockHttpServletResponse = perform.andReturn().response
        val resultString = response.contentAsString

        val parser = Jackson2JsonParser()
        val token = parser.parseMap(resultString)["token"].toString()

        val extractAccountContext = jwtDecoder.decodeJwt(token)
        val tokenUsername = extractAccountContext.username
        val tokenRole = ArrayList(extractAccountContext.authorities)

        //then
        assertThat(tokenUsername).isEqualTo(username)
        assertThat(tokenRole[0].toString()).isEqualTo(AccountRole.ROLE_ADMIN.toString())
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.ROLE_USER): Account {
        var account = Account(accountname = name, password = pw, role = role)

        var savedAccount = accountService.createAccount(account)
        return savedAccount
    }
}
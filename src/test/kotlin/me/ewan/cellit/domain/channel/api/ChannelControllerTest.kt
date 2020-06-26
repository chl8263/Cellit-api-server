package me.ewan.cellit.domain.channel.api

import com.google.common.net.HttpHeaders
import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class ChannelControllerTest : BaseControllerTest() {

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var cellService: CellService

    @Autowired
    lateinit var cellRepository: CellRepository

    @Test
    fun `Create Channel`(){
        //given

        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        mockMvc.perform(post("api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

        //then
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("channelId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("channelName").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("_links.self").exists())
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
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
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.domain.Channel
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.global.common.ConvertQueryToClass
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
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
    lateinit var cellService: CellService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var cellRepository: CellRepository

    @Autowired
    private lateinit var channelRepository: ChannelRepository

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
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(jsonPath("cellId").exists())
                .andExpect(jsonPath("cellName").exists())
                .andExpect(jsonPath("_links.self").exists())

    }

    @Test
    fun `Get Channels with CellId`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName1 = "common1"
        val channel1 = Channel(channelName = channelName1, cell = savedCell)
        channelRepository.save(channel1)

        val channelName2 = "common2"
        val channel2 = Channel(channelName = channelName2, cell = savedCell)
        channelRepository.save(channel2)

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cells/${savedCell.cellId}/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_embedded.channelEntityModelList[0].channelName").value("common"))
                .andExpect(jsonPath("_embedded.channelEntityModelList[1].channelName").value("common1"))
                .andExpect(jsonPath("_embedded.channelEntityModelList[2].channelName").value("common2"))
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Get Cells list with search name`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, name)

        val cellName2 = "Cell_test2"
        val cellDto2 = CellDto(cellName = cellName2)
        val savedCell2 = cellService.createCell(cellDto2, name)

        val cellName3 = "Cell_test3"
        val cellDto3 = CellDto(cellName = cellName3)
        val savedCell3 = cellService.createCell(cellDto3, name)

        val cellName4 = "Cell_test4"
        val cellDto4 = CellDto(cellName = cellName4)
        val savedCell4 = cellService.createCell(cellDto4, name)

        val findName = "Cell"

        //when
        ///api/cells?q=name%3D$findName,id%3D123
        mockMvc.perform(get("/api/cells?query=cellName%3D$findName")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_embedded.cellEntityModelList").exists())
                //.andExpect(jsonPath("_embedded.channelEntityModelList[0].channelName").value("common"))
    }

    @Test
    fun `Fail to Get Cells list with search name and invalid colon`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, name)

        val cellName2 = "Cell_test2"
        val cellDto2 = CellDto(cellName = cellName2)
        val savedCell2 = cellService.createCell(cellDto2, name)

        val cellName3 = "Cell_test3"
        val cellDto3 = CellDto(cellName = cellName3)
        val savedCell3 = cellService.createCell(cellDto3, name)

        val cellName4 = "Cell_test4"
        val cellDto4 = CellDto(cellName = cellName4)
        val savedCell4 = cellService.createCell(cellDto4, name)

        val findName = "Cell"

        //when
        ///api/cells?q=name%3D$findName,id%3D123
        val result = mockMvc.perform(get("/api/cells?query=cellName%6D$findName")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //then
                .andDo(print())
                .andExpect(status().isBadRequest)
                .andReturn()

        assertThat(result.response.getContentAsString()).isEqualTo(ConvertQueryToClass.INVALID_COLON_MSG)
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
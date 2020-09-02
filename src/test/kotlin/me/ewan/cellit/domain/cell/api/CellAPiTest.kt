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
import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.service.CellRequestService
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.CellRole
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

/**
 * Test class for the {@link CellController}
 *
 * @author Ewan
 */
internal class CellAPiTest : BaseControllerTest() {

    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var cellService: CellService

    @Autowired
    lateinit var cellRequestService: CellRequestService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var cellRepository: CellRepository

    @Autowired
    private lateinit var channelRepository: ChannelRepository

    @Autowired
    private lateinit var cellRequestRepository: CellRequestRepository

    @Autowired
    private lateinit var accountCellRepository: AccountCellRepository

    @BeforeEach
    fun setUp() {
        this.accountCellRepository.deleteAll()
        this.accountRepository.deleteAll()
        this.channelRepository.deleteAll()
        this.cellRequestRepository.deleteAll()
        this.cellRepository.deleteAll()
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
        val cellDescription = ""
        val cellDto = CellDto(cellName = cellName, cellDescription = cellDescription)

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
                .andExpect(jsonPath("cellDescription").exists())
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Get Channels with CellId`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val testAccount1 = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, testAccount1.accountname)

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
        val testAccount1 = createAccount(name = name, pw = pw)
        val jwtToken = getJwtToken(name, pw)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1, cellDescription = "Let's talk about Accounting")
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        val cellName2 = "Cell_test2"
        val cellDto2 = CellDto(cellName = cellName2)
        val savedCell2 = cellService.createCell(cellDto2, testAccount1.accountname)

        val cellName3 = "Cell_test3"
        val cellDto3 = CellDto(cellName = cellName3)
        val savedCell3 = cellService.createCell(cellDto3, testAccount1.accountname)

        val cellName4 = "Cell_test4"
        val cellDto4 = CellDto(cellName = cellName4)
        val savedCell4 = cellService.createCell(cellDto4, testAccount1.accountname)

        val findName = "Cell"

        //when
        /*
        *  When request url, must use 3%D between variable and value.
        * */
        mockMvc.perform(get("/api/cells?query=cellName=$findName")
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
        val testAccount1 = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        val cellName2 = "Cell_test2"
        val cellDto2 = CellDto(cellName = cellName2)
        val savedCell2 = cellService.createCell(cellDto2, testAccount1.accountname)

        val cellName3 = "Cell_test3"
        val cellDto3 = CellDto(cellName = cellName3)
        val savedCell3 = cellService.createCell(cellDto3, testAccount1.accountname)

        val cellName4 = "Cell_test4"
        val cellDto4 = CellDto(cellName = cellName4)
        val savedCell4 = cellService.createCell(cellDto4, testAccount1.accountname)

        val findName = "Cell"

        //when
        ///api/cells?q=name%3D$findName,id%3D123
        val result = mockMvc.perform(get("/api/cells?query=cellName-$findName")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //then
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `Insert account in specific cell`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 =createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //when
        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Get accounts from cell`(){

        /*
        * s: Insert account in specific cell
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 =createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //when
        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("_links.self").exists())
        /*
        * e: Insert account in specific cell
        * */

        /*
        * s: Get accounts from cell
        * */
        //when
        mockMvc.perform(get("/api/cells/${savedCell1.cellId}/accounts")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_embedded.accountEntityModelList").exists())
                .andExpect(jsonPath("_embedded.accountEntityModelList[0].accountName").value(name))
                .andExpect(jsonPath("_embedded.accountEntityModelList[1].accountName").value(name2))
        /*
        * e: Get accounts from cell
        * */
    }

    @Test
    fun `Delete account in cell`(){

        /*
        * s: Insert account in specific cell
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 =createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //when
        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("_links.self").exists())
        /*
        * e: Insert account in specific cell
        * */

        /*
        * s: Delete account in cell
        * */
        //when
        mockMvc.perform(delete("/api/cells/${savedCell1.cellId}/account/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_embedded.accountEntityModelList").exists())
//                .andExpect(jsonPath("_embedded.accountEntityModelList[0].accountName").value(name))
//                .andExpect(jsonPath("_embedded.accountEntityModelList[1].accountName").value(name2))
        /*
        * e: Delete account in cell
        * */
    }

    @Test
    fun `Create CellRequest with cell id and account id`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 = createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        //when
        val result = mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Fail to Create CellRequest list with cell id and account id that already requested`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 = createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        val cellRequest = CellRequest(cell = savedCell1, accountId = testAccount2.accountId)
        cellRequestService.createCellRequest(cellRequest)

        //when
        val result = mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `Fail to Create CellRequest list with cell id and account id that already joined in cell`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 = createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        //when
        val result = mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount1.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `Get to CellRequest with cell id`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 = createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        //when
        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //when
        mockMvc.perform(get("/api/cells/${savedCell1.cellId}/cellRequests")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Delete Cell request`() {
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 = createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val jwtToken = getJwtToken(name2, pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        mockMvc.perform(post("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

        //when
        val result = mockMvc.perform(delete("/api/cells/${savedCell1.cellId}/cellRequests/accounts/${testAccount2.accountId}")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )

                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("accountId").value(testAccount2.accountId!!))
                .andExpect(jsonPath("accountName").value(testAccount2.accountname))
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
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
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class ChannelApiTest : BaseControllerTest() {

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
        mockMvc.perform(post("/api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

        //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelId").exists())
                .andExpect(jsonPath("channelName").exists())
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Create Channel Post`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val saveduser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val result = mockMvc.perform(post("/api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelId").exists())
                .andExpect(jsonPath("channelName").exists())
                .andExpect(jsonPath("_links.self").exists())

        //given
        val response: MockHttpServletResponse = result.andReturn().response
        val responseBody = response.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post"
        val channelPostContent = "<p>Hello World!</p>\n" +
                "    <p>Some initial <strong>bold</strong> text</p>\n" +
                "    <p>\n" +
                "        <br/>\n" +
                "    </p>"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = saveduser.accountId!!,
                accountName = saveduser.accountname!!)

        //when
        mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelPostDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelPostId").exists())
                .andExpect(jsonPath("channelPostName").exists())
                .andExpect(jsonPath("accountId").exists())
                .andExpect(jsonPath("accountName").exists())
                .andExpect(jsonPath("viewCount").exists())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("_links.self").exists())
    }

    @Test
    fun `Get page 1 of 10 channel post`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val saveduser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val result = mockMvc.perform(post("/api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelId").exists())
                .andExpect(jsonPath("channelName").exists())
                .andExpect(jsonPath("_links.self").exists())

        //given
        val response: MockHttpServletResponse = result.andReturn().response
        val responseBody = response.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()


        for(i in 0..27){
            val channelPostName = "Test new post Subject $i"
            val channelPostContent = "content $i"
            val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                    channelPostContent = channelPostContent,
                    accountId = saveduser.accountId!!,
                    accountName = saveduser.accountname!!)

            //when
            mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
                    .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(channelPostDto))
            )
                    //then
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(jsonPath("channelPostId").exists())
                    .andExpect(jsonPath("channelPostName").exists())
                    .andExpect(jsonPath("accountId").exists())
                    .andExpect(jsonPath("accountName").exists())
                    .andExpect(jsonPath("createDate").exists())
                    .andExpect(jsonPath("_links.self").exists())
        }

        //when
        mockMvc.perform(get("/api/channels/${channelId}/channelPosts")
                .param("page", "1") //Spring hateoas page start index is 0
                .param("size", "10")
                .param("sort", "createDate,DESC")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_embedded").exists())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("page").exists())
    }

    @Test
    fun `Get channel post content`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val saveduser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val result = mockMvc.perform(post("/api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelId").exists())
                .andExpect(jsonPath("channelName").exists())
                .andExpect(jsonPath("_links.self").exists())

        //given
        val response: MockHttpServletResponse = result.andReturn().response
        val responseBody = response.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post Subject"
        val channelPostContent = "content test"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = saveduser.accountId!!,
                accountName = saveduser.accountname!!)

        //when
        val result2 = mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelPostDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelPostId").exists())
                .andExpect(jsonPath("channelPostName").exists())
                .andExpect(jsonPath("accountId").exists())
                .andExpect(jsonPath("accountName").exists())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("_links.self").exists())

        val response2: MockHttpServletResponse = result2.andReturn().response
        val responseBody2 = response2.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

        //when
        mockMvc.perform(get("/api/channels/${channelId}/channelPosts/${channelPostId}/channelContent")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("channelPostId").value(channelPostId))
                .andExpect(jsonPath("accountName").value(saveduser.accountname))
                .andExpect(jsonPath("channelPostName").value(channelPostName))
                .andExpect(jsonPath("channelPostContent").value(channelPostContent))
    }

    @Test
    fun `Update Channel Content`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val saveduser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val result = mockMvc.perform(post("/api/channels")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelDto))
        )

                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelId").exists())
                .andExpect(jsonPath("channelName").exists())
                .andExpect(jsonPath("_links.self").exists())

        //given
        val response: MockHttpServletResponse = result.andReturn().response
        val responseBody = response.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post Subject"
        val channelPostContent = "content test"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = saveduser.accountId!!,
                accountName = saveduser.accountname!!)

        //when
        val result2 = mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelPostDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelPostId").exists())
                .andExpect(jsonPath("channelPostName").exists())
                .andExpect(jsonPath("accountId").exists())
                .andExpect(jsonPath("accountName").exists())
                .andExpect(jsonPath("createDate").exists())
                .andExpect(jsonPath("_links.self").exists())

        val response2: MockHttpServletResponse = result2.andReturn().response
        val responseBody2 = response2.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

        //given
        val modifiedChannelPostName = "Test new post Subject222222"
        val modifiedChannelPostContent = "content test222222222"
        val modifiedChannelPostDto = ChannelPostDto(channelPostName = modifiedChannelPostName,
                channelPostContent = modifiedChannelPostContent)

        //when
        mockMvc.perform(patch("/api/channels/${channelId}/channelPosts/${channelPostId}/channelContent")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(modifiedChannelPostDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
//                .andExpect(jsonPath("channelPostId").value(channelPostId))
//                .andExpect(jsonPath("accountName").value(saveduser.accountname))
//                .andExpect(jsonPath("channelPostName").value(channelPostName))
//                .andExpect(jsonPath("channelPostContent").value(channelPostContent))
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
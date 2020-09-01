package me.ewan.cellit.domain.channel.api

import com.google.common.net.HttpHeaders
import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.channel.dao.ChannelPostCommentRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostContentRepository
import me.ewan.cellit.domain.channel.dao.ChannelPostRepository
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostCommentDto
import me.ewan.cellit.domain.channel.vo.dto.ChannelPostDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class ChannelApiTest : BaseControllerTest() {

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
    private lateinit var cellRequestRepository: CellRequestRepository

    @Autowired
    private lateinit var channelPostContentRepository: ChannelPostContentRepository

    @Autowired
    private lateinit var channelPostCommentRepository: ChannelPostCommentRepository

    @Autowired
    private lateinit var channelPostRepository: ChannelPostRepository



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
    fun `Update Channel active state`(){
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

        /*
        *  s : Update Channel active state
        * */
        val response2: MockHttpServletResponse = result.andReturn().response
        val responseBody2 = response2.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelId = parser2.parseMap(responseBody2)["channelId"].toString()
        val cellId = parser2.parseMap(responseBody2)["cellId"].toString()

        //given
        val activeState = 0

        //when
        mockMvc.perform(patch("/api/channels/${channelId}/active")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content("$activeState")
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("channelId").value(channelId))
                .andExpect(jsonPath("cellId").value(cellId))
                .andExpect(jsonPath("active").value(activeState))
        /*
        *  e : Update Channel Content
        * */
    }

    @Test
    fun `Create Channel Post`(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

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
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

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
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

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

        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */

        //given
        val response: MockHttpServletResponse = result.andReturn().response
        val responseBody = response.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()


        val nameList = arrayListOf("Test one", "Test two", "Account report", "Account report2", "Supply Chain report",
                "Test one 2", "Test two 2", "Account report 2", "Account report2 2", "Supply Chain report 2", "Supply Chain report 3",
        "Test one 4", "Test two 4", "Account report 4", "Account report2 5", "Supply Chain report 5", "Supply Chain report 5")

        //for(name in nameList){
        for(name in nameList){
            val channelPostName = "$name"
            val channelPostContent = "content $name"
            val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                    channelPostContent = channelPostContent,
                    accountId = savedUser.accountId!!,
                    accountName = savedUser.accountname!!)

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
                .param("postNameToSearch", "report") //Spring hateoas page start index is 0
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

        /*
        *  e : Create Channel Post
        * */
    }

    @Test
    fun `Get channel post content`(){
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

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
        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */
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
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

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

        /*
        *  e : Create Channel Post
        * */

        /*
        *  s : Get Channel Post Content
        * */
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
                .andExpect(jsonPath("accountName").value(savedUser.accountname))
                .andExpect(jsonPath("channelPostName").value(channelPostName))
                .andExpect(jsonPath("channelPostContent").value(channelPostContent))

        /*
        *  e : Get Channel Post Content
        * */
    }

    @Test
    fun `Update Channel Content`(){
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

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
        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */
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
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

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
        /*
        *  e : Create Channel Post
        * */

        /*
        *  s : Update Channel Content
        * */
        val response2: MockHttpServletResponse = result2.andReturn().response
        val responseBody2 = response2.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

        //given
        val modifiedChannelPostName = "Test new post Subject 2"
        val modifiedChannelPostContent = "content test 2"
        val modifiedChannelPostDto = ChannelPostDto(channelPostName = modifiedChannelPostName,
                channelPostContent = modifiedChannelPostContent)

        //when
        mockMvc.perform(patch("/api/channels/${channelId}/channelPosts/${channelPostId}/channelContent")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(modifiedChannelPostDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("channelPostId").value(channelPostId))
                .andExpect(jsonPath("accountName").value(savedUser.accountname))
                .andExpect(jsonPath("channelPostName").value(modifiedChannelPostName))
                .andExpect(jsonPath("channelPostContent").value(modifiedChannelPostContent))
        /*
        *  e : Update Channel Content
        * */
    }

    @Test
    fun `Update Channel Post view Count`(){
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val resultOfCreateChannel = mockMvc.perform(post("/api/channels")
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
        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */
        //given
        val responseOfCreateChannel: MockHttpServletResponse = resultOfCreateChannel.andReturn().response
        val responseBody = responseOfCreateChannel.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post Subject"
        val channelPostContent = "content test"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

        //when
        val resultOfCreateChannelPost = mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
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
        /*
        *  e : Create Channel Post
        * */

        /*
        *  s : Update Channel Post view Count
        * */
        val responseOfUpdateChannelPostViewCount: MockHttpServletResponse = resultOfCreateChannelPost.andReturn().response
        val responseBody2 = responseOfUpdateChannelPostViewCount.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

//        //given
//        val modifiedChannelPostName = "Test new post Subject 2"
//        val modifiedChannelPostContent = "content test 2"
//        val modifiedChannelPostDto = ChannelPostDto(channelPostName = modifiedChannelPostName,
//                channelPostContent = modifiedChannelPostContent)

        val viewCount = 1

        //when
        mockMvc.perform(patch("/api/channels/${channelId}/channelPosts/${channelPostId}/viewCount")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content("$viewCount")
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("channelPostId").value(channelPostId))
                .andExpect(jsonPath("accountName").value(savedUser.accountname))
                .andExpect(jsonPath("channelPostName").value(channelPostName))
                .andExpect(jsonPath("viewCount").value(viewCount))
        /*
        *  e : Update Channel Content
        * */
    }

    @Test
    fun `Create Channel Post Comment`(){
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val resultOfCreateChannel = mockMvc.perform(post("/api/channels")
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
        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */
        //given
        val responseOfCreateChannel: MockHttpServletResponse = resultOfCreateChannel.andReturn().response
        val responseBody = responseOfCreateChannel.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post Subject"
        val channelPostContent = "content test"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

        //when
        val resultOfCreateChannelPost = mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
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
        /*
        *  e : Create Channel Post
        * */

        /*
        *  s : Create Channel Post Comment
        * */
        val responseOfChannelPost: MockHttpServletResponse = resultOfCreateChannelPost.andReturn().response
        val responseBody2 = responseOfChannelPost.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

        val channelComment = "Good Job!"
        val channelPostCommentDto = ChannelPostCommentDto(accountId = savedUser.accountId, channelPostComment = channelComment)

        //when
        mockMvc.perform(post("/api/channels/${channelId}/channelPosts/${channelPostId}/channelPostComments")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(channelPostCommentDto))
        )
                //then
                .andDo(print())
                .andExpect(status().isCreated)
                .andExpect(jsonPath("channelPostCommentId").isNotEmpty)
                .andExpect(jsonPath("channelPostComment").value(channelComment))
                .andExpect(jsonPath("accountId").value(savedUser.accountId!!))
                .andExpect(jsonPath("channelPostId").value(channelPostId))
        /*
        *  e : Create Channel Post Comment
        * */
    }

    @Test
    fun `Get Channel Post Comments`(){
        /*
        *  s : Create Channel
        * */
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val savedUser = createAccount(name = name, pw = pw)

        val jwtToken = getJwtToken(name, pw)

        val cellName = "Accounting"
        val cellDto = CellDto(cellName = cellName)
        val savedCell = cellService.createCell(cellDto, name)

        val channelName = "common"
        val channelDto = ChannelDto(cellId = savedCell.cellId ,channelName = channelName)

        //when
        val resultOfCreateChannel = mockMvc.perform(post("/api/channels")
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
        /*
        *  e : Create Channel
        * */

        /*
        *  s : Create Channel Post
        * */
        //given
        val responseOfCreateChannel: MockHttpServletResponse = resultOfCreateChannel.andReturn().response
        val responseBody = responseOfCreateChannel.contentAsString
        val parser = Jackson2JsonParser()
        val channelId = parser.parseMap(responseBody)["channelId"].toString()
        val cellId = parser.parseMap(responseBody)["cellId"].toString()

        val channelPostName = "Test new post Subject"
        val channelPostContent = "content test"
        val channelPostDto = ChannelPostDto(channelPostName = channelPostName,
                channelPostContent = channelPostContent,
                accountId = savedUser.accountId!!,
                accountName = savedUser.accountname!!)

        //when
        val resultOfCreateChannelPost = mockMvc.perform(post("/api/channels/${channelId}/channelPosts")
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
        /*
        *  e : Create Channel Post
        * */

        /*
        *  s : Create Channel Post Comment
        * */
        val responseOfChannelPost: MockHttpServletResponse = resultOfCreateChannelPost.andReturn().response
        val responseBody2 = responseOfChannelPost.contentAsString
        val parser2 = Jackson2JsonParser()
        val channelPostId = parser2.parseMap(responseBody2)["channelPostId"].toString()

        val channelComments = arrayOf("Good Job!", "Good Job! 2", "Good Job! 3", "Good Job! 4", "Good Job! 5")

        channelComments.forEach {
            val channelPostCommentDto = ChannelPostCommentDto(accountId = savedUser.accountId, channelPostComment = it)

            //when
            mockMvc.perform(post("/api/channels/${channelId}/channelPosts/${channelPostId}/channelPostComments")
                    .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(channelPostCommentDto))
            )
                    //then
                    .andDo(print())
                    .andExpect(status().isCreated)
                    .andExpect(jsonPath("channelPostCommentId").isNotEmpty)
                    .andExpect(jsonPath("channelPostComment").value(it))
                    .andExpect(jsonPath("accountId").value(savedUser.accountId!!))
                    .andExpect(jsonPath("channelPostId").value(channelPostId))
        }

        /*
        *  e : Create Channel Post Comment
        * */

        /*
        *  s : Get Channel Post Comments
        * */
        //when
        mockMvc.perform(get("/api/channels/${channelId}/channelPosts/${channelPostId}/channelPostComments")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtToken)
                .accept(MediaTypes.HAL_JSON)
        )
                //then
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("_embedded.channelPostCommentEntityModelList").exists())
        /*
        *  e : Get Channel Post Comments
        * */
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
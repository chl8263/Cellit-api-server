package me.ewan.cellit.common

import com.fasterxml.jackson.databind.ObjectMapper
import jdk.nashorn.internal.ir.annotations.Ignore
import me.ewan.cellit.global.common.AppProperties
import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.common.util.Jackson2JsonParser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Ignore
class BaseControllerTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var appProperties: AppProperties

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    companion object{
        const val BEARER_PREFIX: String = "Bearer "
    }

    protected fun getJwtToken(username: String, pw: String): String{
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
}
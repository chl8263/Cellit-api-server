package me.ewan.cellit.global.security.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import me.ewan.cellit.global.security.AccountContext
import me.ewan.cellit.global.security.JwtFactory
import me.ewan.cellit.global.security.dtos.TokenDto
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import org.codehaus.jackson.JsonProcessingException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class FormLoginAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    @Autowired
    private lateinit var factory: JwtFactory

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    /*
    * This method get PostAuthorizationToken from authenticate of FormLoginAuthenticationProvider -> successfulAuthentication of FormLoginFilter
    * */
    override fun onAuthenticationSuccess(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {
        val token = auth as PostAuthorizationToken
        val context = token.principal as AccountContext
        val tokenString = factory.generateToken(context)
        processResponse(res, writeDto(tokenString))
    }

    private fun writeDto(token: String): TokenDto = TokenDto(token)

    @Throws(JsonProcessingException::class, IOException::class)
    private fun processResponse(res: HttpServletResponse, dto: TokenDto){
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.OK.value()
        res.writer.write(objectMapper.writeValueAsString(dto))
    }
}
package me.ewan.cellit.global.security.filters

import com.fasterxml.jackson.databind.ObjectMapper
import me.ewan.cellit.global.exception.dtos.ErrorDto
import me.ewan.cellit.global.security.HeaderTokenExtractor
import me.ewan.cellit.global.security.JwtDecoder
import me.ewan.cellit.global.security.JwtProperties.HEADER_STRING
import me.ewan.cellit.global.security.JwtProperties.TOKEN_PREFIX
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, private val extractor: HeaderTokenExtractor, private val decoder: JwtDecoder)
    : BasicAuthenticationFilter(authenticationManager) {

//    @Autowired
//    private lateinit var objectMapper: ObjectMapper

//    @Autowired
//    private lateinit var extractor: HeaderTokenExtractor
//
//    @Autowired
//    private lateinit var decoder: JwtDecoder

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val tokenPayload = request.getHeader(HEADER_STRING)

        println( "!!!!" + tokenPayload)

        if(tokenPayload.isNullOrBlank() || !tokenPayload.startsWith(TOKEN_PREFIX)){

            val objectMapper = ObjectMapper()

            val errorDto = ErrorDto("UNAUTHORIZED", "UNAUTHORIZED")

            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.writer.write(objectMapper.writeValueAsString(errorDto))
            //chain.doFilter(request, response)
            return
        }

        val accountContext = decoder.decodeJwt(extractor.extract(tokenPayload))

        println( "@@@@@@@@" + accountContext.authorities)

        val token = PostAuthorizationToken(accountContext.account, accountContext.password, accountContext.authorities)
        //val authentication = authenticationManager.authenticate(token)

        SecurityContextHolder.getContext().authentication = token

        println( "@@@@@#@#@#@@@@" + SecurityContextHolder.getContext().authentication)

        chain.doFilter(request, response)
    }

}
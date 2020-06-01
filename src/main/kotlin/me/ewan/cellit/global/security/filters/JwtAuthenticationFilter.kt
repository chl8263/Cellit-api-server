package me.ewan.cellit.global.security.filters

import me.ewan.cellit.global.security.dtos.JwtAuthenticationDto
import me.ewan.cellit.global.security.tokens.PreAuthorizationToken
import org.codehaus.jackson.map.ObjectMapper
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(defaultFilterProcessesUrl: String?) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {

    private var myAuthenticationSuccessHandler: AuthenticationSuccessHandler? = null
    private var myAuthenticationFailureHandler: AuthenticationFailureHandler? = null

    constructor(defaultFilterProcessesUrl: String, authenticationSuccessHandler: AuthenticationSuccessHandler, authenticationFailureHandler: AuthenticationFailureHandler) : this(defaultFilterProcessesUrl){
        this.myAuthenticationSuccessHandler = authenticationSuccessHandler
        this.myAuthenticationFailureHandler = authenticationFailureHandler
    }

    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication {

        val formLoginDto = ObjectMapper().readValue(req.reader, JwtAuthenticationDto::class.java)

        val token = PreAuthorizationToken(formLoginDto)

        return super.getAuthenticationManager().authenticate(token)
    }

    /*
    * Try authentication at FormLoginAuthenticationProvider
    * This method perform to make JWT token and inject to response
    * */
    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        this.myAuthenticationSuccessHandler?.onAuthenticationSuccess(request, response, authResult)
    }

    /*
    * Try authentication at FormLoginAuthenticationProvider
    * This method perform to handle error about FormLoginAuthenticationProvider result
    * */
    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        //super.unsuccessfulAuthentication(request, response, failed)
        this.myAuthenticationFailureHandler?.onAuthenticationFailure(request, response, failed)
    }
}
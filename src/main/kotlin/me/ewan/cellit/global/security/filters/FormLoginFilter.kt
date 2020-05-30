package me.ewan.cellit.global.security.filters

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FormLoginFilter(defaultFilterProcessesUrl: String?) : AbstractAuthenticationProcessingFilter(defaultFilterProcessesUrl) {



    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    * Try authentication at FormLoginAuthenticationProvider
    * This method perform to make JWT token and inject to response
    * */
    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        super.successfulAuthentication(request, response, chain, authResult)
    }

    /*
    * Try authentication at FormLoginAuthenticationProvider
    * This method perform to handle error about FormLoginAuthenticationProvider result
    * */
    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        super.unsuccessfulAuthentication(request, response, failed)
    }
}
package me.ewan.cellit.global.security.filters

import me.ewan.cellit.global.security.HeaderTokenExtractor
import me.ewan.cellit.global.security.handlers.JwtAuthenticationFailureHandler
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import java.security.Security
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(reqMatcher: RequestMatcher) : AbstractAuthenticationProcessingFilter(reqMatcher) {

    private val logger = KotlinLogging.logger {}

    private var failureHandler: JwtAuthenticationFailureHandler? = null

    private var extractor: HeaderTokenExtractor? = null

    constructor(reqMatcher: RequestMatcher, failureHandler: JwtAuthenticationFailureHandler, extractor: HeaderTokenExtractor) : this(reqMatcher){
        this.failureHandler = failureHandler
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        TODO()
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {

        val context = SecurityContextHolder.createEmptyContext()

        context.authentication = authResult
        SecurityContextHolder.setContext(context)

        chain?.doFilter(request, response)
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {

        SecurityContextHolder.clearContext() // must call this cause this request was never authentication.

        this.unsuccessfulAuthentication(request,response, failed)
    }
}
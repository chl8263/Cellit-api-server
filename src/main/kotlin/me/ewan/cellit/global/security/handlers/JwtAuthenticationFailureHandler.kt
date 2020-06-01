package me.ewan.cellit.global.security.handlers

import mu.KotlinLogging
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val log = KotlinLogging.logger {}

    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
        // TODO : Send response as error message with DTO
        log.error { exception?.message }
    }
}
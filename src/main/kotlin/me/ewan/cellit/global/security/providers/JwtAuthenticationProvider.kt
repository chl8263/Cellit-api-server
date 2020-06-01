package me.ewan.cellit.global.security.providers

import me.ewan.cellit.global.security.JwtDecoder
import me.ewan.cellit.global.security.tokens.JwtPreProcessingToken
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AuthenticationProvider{

    @Autowired
    private lateinit var jwtDecoder: JwtDecoder

    override fun authenticate(authentication: Authentication): Authentication {

        val token = authentication.principal as String

        val accountContext = jwtDecoder.decodeJwt(token)

        return PostAuthorizationToken.getTokenFromAccountContext(accountContext)
    }

    override fun supports(authentication: Class<*>?): Boolean
            = JwtPreProcessingToken::class.java.isAssignableFrom(authentication)
}
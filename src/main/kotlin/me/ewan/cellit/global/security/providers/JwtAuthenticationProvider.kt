package me.ewan.cellit.global.security.providers

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.global.security.AccountContext
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import me.ewan.cellit.global.security.tokens.PreAuthorizationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider: AuthenticationProvider {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun authenticate(authentication: Authentication?): Authentication { // make authentication object

        val token = authentication as PreAuthorizationToken

        val username = token.getUsername()
        val password = token.getPassword()

        val account = accountService.getAccount(username)

        if(isCorrectPassword(password, account)){
            return PostAuthorizationToken.getTokenFromAccountContext(AccountContext.fromAccountModel(account))
        }

        throw NoSuchElementException("Authentication information is now correct.")
    }

    override fun supports(authentication: Class<*>?): Boolean = // support which object
            PreAuthorizationToken::class.java.isAssignableFrom(authentication)


    private fun isCorrectPassword(password: String, account: Account): Boolean{
        return passwordEncoder.matches(password, account.password)
    }
}
package me.ewan.cellit.global.config

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.global.common.AppProperties
import me.ewan.cellit.global.common.ApplicationContextProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@EnableAuthorizationServer
class AuthServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManagerBean: AuthenticationManager

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var tokenStore: TokenStore

    @Autowired
    private lateinit var appProperties: AppProperties

    private val CLIETN_ID by lazy { appProperties.clientId }
    private val CLIENT_CECRET by lazy {appProperties.clientSecret}
    private val AUTHORIZED_GRANT_TYPES_1 by lazy { "password" }
    private val AUTHORIZED_GRANT_TYPES_2 by lazy { "refresh_token" }
    private val SCOPE_1 by lazy { "read" }
    private val SCOPE_2 by lazy { "write" }
    private val ACCESS_TOKEN_VALIDITY_SECONDS by lazy { 10 * 60 }
    private val REFRESH_TOKEN_VALIDITY_SECONDS by lazy { 6 * 10 * 60 }


    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security?.passwordEncoder(passwordEncoder)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.let {
            it.inMemory()
                    .withClient(CLIETN_ID)
                    .authorizedGrantTypes(AUTHORIZED_GRANT_TYPES_1, AUTHORIZED_GRANT_TYPES_2)
                    .scopes(SCOPE_1, SCOPE_2)
                    .secret(this.passwordEncoder.encode(CLIENT_CECRET))
                    .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                    .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS)

        }
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints?.let{
            it.authenticationManager(authenticationManagerBean)
                    .userDetailsService(accountService)
                    .tokenStore(tokenStore)
        }
    }
}
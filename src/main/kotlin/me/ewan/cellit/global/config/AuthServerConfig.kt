package me.ewan.cellit.global.config

import me.ewan.cellit.domain.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
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
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var authenticationManagerBean: AuthenticationManager

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var tokenStore: TokenStore

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security?.passwordEncoder(passwordEncoder)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.let {
            it.inMemory()
                    .withClient("myApp")
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("read", "write")
                    .secret(this.passwordEncoder.encode("pass"))
                    .accessTokenValiditySeconds(10 * 60)
                    .refreshTokenValiditySeconds(6 * 10 * 60)

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
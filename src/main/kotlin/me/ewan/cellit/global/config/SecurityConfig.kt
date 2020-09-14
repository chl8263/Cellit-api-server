/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.global.config

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.global.security.HeaderTokenExtractor
import me.ewan.cellit.global.security.JwtDecoder
import me.ewan.cellit.global.security.filters.JwtAuthenticationFilter
import me.ewan.cellit.global.security.filters.JwtAuthorizationFilter
import me.ewan.cellit.global.security.handlers.JwtAuthenticationFailureHandler
import me.ewan.cellit.global.security.handlers.JwtAuthenticationSuccessHandler
import me.ewan.cellit.global.security.providers.JwtAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * @author Ewan
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var jwtAuthenticationProvider: JwtAuthenticationProvider

    @Autowired
    private lateinit var formLoginAuthenticationSuccessHandler: JwtAuthenticationSuccessHandler

    @Autowired
    private lateinit var jwtAuthenticationFailureHandler: JwtAuthenticationFailureHandler

    @Autowired
    private lateinit var extractor: HeaderTokenExtractor

    @Autowired
    private lateinit var decoder: JwtDecoder

    @Throws(Exception::class)
    protected fun formLoginFilter(): JwtAuthenticationFilter {
        val filter = JwtAuthenticationFilter("/auth", formLoginAuthenticationSuccessHandler, jwtAuthenticationFailureHandler)
        filter.setAuthenticationManager(authenticationManagerBean())
        return filter
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {

        auth?.authenticationProvider(this.jwtAuthenticationProvider)

        auth?.userDetailsService(accountService)
                ?.passwordEncoder(passwordEncoder)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web?.let {
        }
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.headers().frameOptions().disable()
        http.addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http
                .addFilter(JwtAuthorizationFilter(authenticationManager(), extractor, decoder))
                .authorizeRequests()
                .mvcMatchers("/", "/signUp", "/login**", "/mainBoard").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/accounts").permitAll()
                .mvcMatchers("/admin**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
        http.cors()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource{
        val configuration = CorsConfiguration()
        configuration.addAllowedOrigin("http://localhost:8080")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials =true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
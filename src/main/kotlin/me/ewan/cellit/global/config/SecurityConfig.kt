package me.ewan.cellit.global.config

import me.ewan.cellit.domain.account.handler.LoginFailureHandler
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.session.HttpSessionEventPublisher

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun authenticationFailureHandler() : AuthenticationFailureHandler = LoginFailureHandler()

    @Bean
    fun sessionRegistry() : SessionRegistry = SessionRegistryImpl()

    @Bean
    fun httpSessionEventPublisher(): ServletListenerRegistrationBean<HttpSessionEventPublisher> = ServletListenerRegistrationBean(HttpSessionEventPublisher())


    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web?.let {
            //it.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            it.ignoring()
                    .antMatchers("/assets/**")
                    .antMatchers("/dist/**")
                    .antMatchers("/images/**")
        }
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        http?.let {
            it.authorizeRequests()
                    .mvcMatchers("/signUp", "/login**","/loginError").permitAll()
                    .mvcMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()

            it.formLogin()
                    .loginPage("/login")
                    .failureHandler(authenticationFailureHandler())
                    .permitAll()

            it.httpBasic()

            it.logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
//                    .deleteCookies("JSESSIONID")
//                    .invalidateHttpSession(true)

            it.sessionManagement()
                    .sessionFixation()
                    .changeSessionId()
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)

            it.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

        }
    }
}
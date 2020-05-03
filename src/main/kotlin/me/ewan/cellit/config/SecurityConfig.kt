package me.ewan.cellit.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import java.lang.Exception

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

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
                    .mvcMatchers("/signUp").permitAll()
                    .mvcMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()

            it.formLogin()
                    .loginPage("/login").permitAll()

            it.httpBasic()

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
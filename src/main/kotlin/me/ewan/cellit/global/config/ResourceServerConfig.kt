package me.ewan.cellit.global.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter(){

    @Autowired
    lateinit var authenticationFailureHandler: AuthenticationFailureHandler

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId("event")
    }

//    override fun configure(http: HttpSecurity?) {
//        http?.let{
//            it.anonymous()
//                    .and()
//                    .authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET, "/api/**")
//                        .anonymous()
//                    .anyRequest()
//                        .authenticated()
//                    .and()
//                    .exceptionHandling()
//                        .accessDeniedHandler(OAuth2AccessDeniedHandler())
//        }
//        http?.let {
//            it.authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
//                    .mvcMatchers("/signUp", "/login**", "/loginError", "logout").permitAll()
//                    .mvcMatchers("/admin").hasRole("ADMIN")
//                    .anyRequest().authenticated()
//                    .and()
//                    .exceptionHandling()
//                    .accessDeniedHandler(OAuth2AccessDeniedHandler())
//
//            it.formLogin()
//                    .loginPage("/login")
//                    .failureHandler(authenticationFailureHandler)
//                    .permitAll()
//
//            it.httpBasic()
//
//            it.logout()
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/")
////                    .deleteCookies("JSESSIONID")
////                    .invalidateHttpSession(true)
//
//            it.sessionManagement()
//                    .sessionFixation()
//                    .changeSessionId()
//                    .invalidSessionUrl("/login")
//                    .maximumSessions(1)
//                    .maxSessionsPreventsLogin(true)
//
//            it.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//        }
//    }
}
package me.ewan.cellit.global.security.handlers

import me.ewan.cellit.global.security.JwtFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class FormLoginAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    @Autowired
    private lateinit var factory: JwtFactory

    /*
    * This method get PostAuthorizationToken from authenticate of FormLoginAuthenticationProvider -> successfulAuthentication of FormLoginFilter
    * */
    override fun onAuthenticationSuccess(req: HttpServletRequest?, res: HttpServletResponse?, auth: Authentication?) {
        // TODO write JWT value on response writer
    }
}
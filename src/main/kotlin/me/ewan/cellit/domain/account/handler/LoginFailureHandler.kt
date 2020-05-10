package me.ewan.cellit.domain.account.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginFailureHandler : AuthenticationFailureHandler {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {

        request?.setAttribute("errorMsg", exception?.message)
        request?.setAttribute("username", request.getParameter("username"))

        request?.getRequestDispatcher("/loginError")?.forward(request, response)
    }
}
package me.ewan.cellit.account

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginFailureHandler : AuthenticationFailureHandler {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {

//        response?.status = HttpStatus.UNAUTHORIZED.value()
//        response?.contentType = "text/html; charset=UTF-8"
//        val data = mapOf("exception" to exception?.message.toString())
//        response?.outputStream?.println(objectMapper.writeValueAsString(data))
        println(exception?.message)
        //request?.setAttribute("error", exception?.message)
        //request?.getRequestDispatcher("/login?error=sex")?.forward(request, response)
        response?.sendRedirect("/login?error=${exception?.message}")
    }
}
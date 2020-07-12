package me.ewan.cellit.global.security.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import me.ewan.cellit.global.error.ErrorHelper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val log = KotlinLogging.logger {}

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    override fun onAuthenticationFailure(req: HttpServletRequest, res: HttpServletResponse, exception: AuthenticationException?) {
        log.error { exception?.message }

        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.BAD_REQUEST.value()
        res.writer.write(objectMapper.writeValueAsString(errorHelper.getUnexpectError("aaa")))
    }
}
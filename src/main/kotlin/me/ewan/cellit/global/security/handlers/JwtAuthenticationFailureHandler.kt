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

/**
 *
 * @author Ewan
 */
@Component
class JwtAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val log = KotlinLogging.logger {}

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    /**
     * Try authentication at BasicAuthenticationFilter.
     * This method perform is to extract jwt token in http Authorization.
     *
     * @author Ewan
     * @param req
     * @param res
     * @param exception
     * @return
     */
    override fun onAuthenticationFailure(req: HttpServletRequest, res: HttpServletResponse, exception: AuthenticationException?) {
        log.error { exception?.message }

        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.BAD_REQUEST.value()
        res.writer.write(objectMapper.writeValueAsString(errorHelper.getUnexpectError("aaa")))
    }
}
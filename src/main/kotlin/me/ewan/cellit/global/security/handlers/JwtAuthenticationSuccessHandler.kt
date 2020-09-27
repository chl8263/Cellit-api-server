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
import me.ewan.cellit.global.security.AccountContext
import me.ewan.cellit.global.security.JwtFactory
import me.ewan.cellit.global.security.dtos.TokenDto
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import org.codehaus.jackson.JsonProcessingException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Ewan
 */
@Component
class JwtAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    @Autowired
    private lateinit var factory: JwtFactory

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    /**
     * This method get PostAuthorizationToken from authenticate of FormLoginAuthenticationProvider -> successfulAuthentication of FormLoginFilter.
     *
     * @author Ewan
     * @param req
     * @param res
     * @param auth
     * @return
     */
    override fun onAuthenticationSuccess(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {
        val token = auth as PostAuthorizationToken
        val context = token.principal as AccountContext
        val tokenString = factory.generateToken(context)
        processResponse(res, writeDto(tokenString))
    }

    private fun writeDto(token: String): TokenDto = TokenDto(token)

    /**
     * Write content type and status at Response.
     *
     * @author Ewan
     * @param req
     * @param res
     * @param auth
     * @return
     */
    @Throws(JsonProcessingException::class, IOException::class)
    private fun processResponse(res: HttpServletResponse, dto: TokenDto){
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.OK.value()
        res.writer.write(objectMapper.writeValueAsString(dto))
    }
}
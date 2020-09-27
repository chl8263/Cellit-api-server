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

package me.ewan.cellit.global.security.filters

import me.ewan.cellit.global.security.HeaderTokenExtractor
import me.ewan.cellit.global.security.JwtDecoder
import me.ewan.cellit.global.security.JwtProperties.HEADER_STRING
import me.ewan.cellit.global.security.JwtProperties.BEARER_PREFIX
import me.ewan.cellit.global.security.tokens.PostAuthorizationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Ewan
 */
class JwtAuthorizationFilter(authenticationManager: AuthenticationManager, private val extractor: HeaderTokenExtractor, private val decoder: JwtDecoder)
    : BasicAuthenticationFilter(authenticationManager) {

    /**
     * Try authentication at BasicAuthenticationFilter.
     * This method perform is to extract jwt token in http Authorization.
     *
     * @author Ewan
     * @param request
     * @param response
     * @param chain
     * @return
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val tokenPayload = request.getHeader(HEADER_STRING)

        if(tokenPayload.isNullOrBlank() || !tokenPayload.startsWith(BEARER_PREFIX)){

            chain.doFilter(request, response)
            return
        }

        val accountContext = decoder.decodeJwt(extractor.extract(tokenPayload))
        val token = PostAuthorizationToken(accountContext.username, accountContext.password, accountContext.authorities)
        SecurityContextHolder.getContext().authentication = token
        chain.doFilter(request, response)
    }

}
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

package me.ewan.cellit.global.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.global.security.JwtProperties.ISSUER
import me.ewan.cellit.global.security.JwtProperties.SECRET
import me.ewan.cellit.global.security.JwtProperties.USER_NAME
import me.ewan.cellit.global.security.JwtProperties.USER_ROLE
import mu.KLogging
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.jwt.Jwt
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException

/**
 *
 * @author Ewan
 */
@Component
class JwtFactory {

    private val log = KotlinLogging.logger {}

    @Throws(UnsupportedEncodingException::class)
    fun generateToken(context: AccountContext): String{
        var token = ""
        try{
            token = JWT.create()
                    .withIssuer(ISSUER)
                    .withClaim(USER_NAME, context.account?.accountname)
                    .withClaim(USER_ROLE, context.account?.role?.name)
                    .sign(generateAlgorithm(SECRET))
        }catch (e: Exception){
            log.error { e.message }
        }

        return token
    }


    private fun generateAlgorithm(signInKey: String): Algorithm = Algorithm.HMAC256(signInKey)
}
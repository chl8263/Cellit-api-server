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
import com.auth0.jwt.interfaces.DecodedJWT
import me.ewan.cellit.global.security.JwtProperties.SECRET
import me.ewan.cellit.global.security.JwtProperties.USER_NAME
import me.ewan.cellit.global.security.JwtProperties.USER_ROLE
import mu.KotlinLogging
import org.springframework.stereotype.Component

/**
 *
 * @author Ewan
 */
@Component
class JwtDecoder {

    private val log = KotlinLogging.logger {}

    /**
     * Decode Jwt Token then parse AccountContext.
     *
     * @author Ewan
     * @param token
     * @return String
     */
    fun decodeJwt(token: String): AccountContext{
        val decodedJWT: DecodedJWT = isValidToken(token)

        val userName = decodedJWT.getClaim(USER_NAME).asString()
        val role = decodedJWT.getClaim(USER_ROLE).asString()

        return AccountContext(userName, "****", role)
    }

    /**
     * valid token.
     *
     * @author Ewan
     * @param token
     * @return String
     */
    private fun isValidToken(token: String) : DecodedJWT {

        lateinit var jwt: DecodedJWT

        try{
            val algorithm = Algorithm.HMAC256(SECRET)
            val verifier = JWT.require(algorithm).build()
            jwt = verifier.verify(token)
        }catch (e: Exception){
            log.error { e.message }
        }

        return jwt
    }
}
package me.ewan.cellit.global.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import me.ewan.cellit.global.security.JwtProperties.SECRET
import me.ewan.cellit.global.security.JwtProperties.USER_NAME
import me.ewan.cellit.global.security.JwtProperties.USER_ROLE
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class JwtDecoder {

    private val log = KotlinLogging.logger {}

    fun decodeJwt(token: String): AccountContext{
        val decodedJWT: DecodedJWT = isValidToken(token)

        val userName = decodedJWT.getClaim(USER_NAME).asString()
        val role = decodedJWT.getClaim(USER_ROLE).asString()

        return AccountContext(userName, "****", role)
    }

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
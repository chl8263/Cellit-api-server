package me.ewan.cellit.global.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import me.ewan.cellit.domain.account.domain.Account
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
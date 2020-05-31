package me.ewan.cellit.global.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import me.ewan.cellit.domain.account.domain.Account
import mu.KLogging
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.jwt.Jwt
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException

@Component
class JwtFactory {

    private val logger = KotlinLogging.logger {}

    companion object{
        val signInKey = "JwtTest"
    }

    @Throws(UnsupportedEncodingException::class)
    fun generateToken(context: AccountContext): String{
        var token = ""
        try{
            token = JWT.create()
                    .withIssuer("cellit")
                    .withClaim("USER_ROLE", context.account.role.name)
                    .sign(generateAlgorithm(JwtFactory.signInKey))
        }catch (e: Exception){
            logger.error { e.message }
        }

        return token
    }


    private fun generateAlgorithm(signInKey: String): Algorithm = Algorithm.HMAC256(signInKey)
}
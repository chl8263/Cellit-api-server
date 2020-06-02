package me.ewan.cellit.global.security

import me.ewan.cellit.global.exception.InvalidJwtException
import org.springframework.stereotype.Component

@Component
class HeaderTokenExtractor {

    companion object{
        val HEADER_PREFIX = "Bearer "
    }

    fun extract(header: String): String{
        if(header.isEmpty() || header.length < HEADER_PREFIX.length){
            throw InvalidJwtException("This is not valid token information")
        }

        return header.substring(HEADER_PREFIX.length, header.length)
    }

}
package me.ewan.cellit.global.security.tokens

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtPreProcessingToken(principal: Any?, credentials: Any?) : UsernamePasswordAuthenticationToken(principal, credentials) {

    constructor(token: String) : this(token, token.length)
}
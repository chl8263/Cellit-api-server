package me.ewan.cellit.global.security.tokens

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class PreAuthorizationToken(username: String?, password: String?) : UsernamePasswordAuthenticationToken(username, password) {

    fun getUsername(): String{
        return super.getPrincipal() as String
    }

    fun getPassword(): String{
        return super.getCredentials() as String
    }
}
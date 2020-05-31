package me.ewan.cellit.global.security.tokens

import me.ewan.cellit.domain.account.dtos.FormLoginDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class PreAuthorizationToken(username: String?, password: String?) : UsernamePasswordAuthenticationToken(username, password) {

    constructor(dto: FormLoginDto) : this(dto.accountname, dto.password)

    fun getUsername(): String{
        return super.getPrincipal() as String
    }

    fun getPassword(): String{
        return super.getCredentials() as String
    }
}
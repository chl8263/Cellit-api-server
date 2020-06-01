package me.ewan.cellit.global.security.tokens

import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.global.security.AccountContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtPostProcessingToken(principal: Any?, credentials: Any?, authorities: Collection<out GrantedAuthority>?)
    : UsernamePasswordAuthenticationToken(principal, credentials, authorities) {

    constructor(username: String, role: AccountRole) : this(username, "****", mutableListOf(role).map { x -> SimpleGrantedAuthority(x.name)})

    fun getUsername(): String = super.getPrincipal() as String
    fun getPassword(): String = super.getCredentials() as String
}
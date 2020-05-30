package me.ewan.cellit.global.security.tokens

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class PostAuthorizationToken(username: String?, password: String?, authorities: MutableCollection<out GrantedAuthority>?)
    : UsernamePasswordAuthenticationToken(username, password, authorities) {
}
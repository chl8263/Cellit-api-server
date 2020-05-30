package me.ewan.cellit.global.security.tokens

import me.ewan.cellit.global.security.AccountContext
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class PostAuthorizationToken(principal: Any?, credentials: Any?, authorities: MutableCollection<out GrantedAuthority>?)
    : UsernamePasswordAuthenticationToken(principal, credentials, authorities) {

    companion object{
        fun getTokenFromAccountContext(context: AccountContext): PostAuthorizationToken
                = PostAuthorizationToken(context, context.password, context.authorities)
    }
}

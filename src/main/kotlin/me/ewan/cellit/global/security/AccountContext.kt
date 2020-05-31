package me.ewan.cellit.global.security

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class AccountContext(val account: Account, username: String?, password: String?, authorities: List<SimpleGrantedAuthority>)
    : User(username, password, authorities) {

    companion object{
        fun fromAccountModel(account: Account): AccountContext{
            return AccountContext(account, account.accountname, account.password, parseAuthorities(account.role))
        }

        private fun parseAuthorities(role: AccountRole): List<SimpleGrantedAuthority> {
            return mutableListOf(role).map { x -> SimpleGrantedAuthority(x.name) }
        }
    }
}
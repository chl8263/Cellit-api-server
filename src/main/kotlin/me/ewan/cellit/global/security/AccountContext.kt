package me.ewan.cellit.global.security

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User


class AccountContext : User {

    var account: Account? = null

    private constructor(account: Account?, username: String, password: String, authorities: Collection<GrantedAuthority?>) : super(username, password, authorities) {
        this.account = account
    }

    constructor(username: String?, password: String?, role: String) : super(username, password, parseAuthorities(role))

    companion object{
        fun fromAccountModel(account: Account): AccountContext =
                AccountContext(account, account.accountname, account.password, parseAuthorities(account.role))

        private fun parseAuthorities(role: AccountRole): List<GrantedAuthority> = mutableListOf(role).map { x -> SimpleGrantedAuthority(x.name) }

        private fun parseAuthorities(role: String): List<GrantedAuthority> = parseAuthorities(AccountRole.getRoleByName(role))
    }
}

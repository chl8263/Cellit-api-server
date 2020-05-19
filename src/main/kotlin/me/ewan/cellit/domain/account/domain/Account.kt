package me.ewan.cellit.domain.account.domain

import me.ewan.cellit.domain.cell.domain.AccountCell
import javax.persistence.*

@Entity
data class Account(
        @Id @GeneratedValue
        var accountId: Long? = null,

        @Column(unique = true)
        var accountname: String,

        var password: String,

        //@ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        var role: AccountRole = AccountRole.USER,

        @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
        var accountCells: MutableList<AccountCell> = mutableListOf()
)
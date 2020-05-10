package me.ewan.cellit.domain.account.domain

import me.ewan.cellit.domain.cell.domain.AccountCell
import javax.persistence.*

@Entity
data class Account(
        @Id @GeneratedValue
        var accountId: Long? = null,

        @Column(unique = true)
        var username: String,

        var password: String,

        var role: String? = null,

        @OneToMany(mappedBy = "account")
        var cells: MutableSet<AccountCell> = mutableSetOf()
)
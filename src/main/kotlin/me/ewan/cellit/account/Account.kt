package me.ewan.cellit.account

import me.ewan.cellit.cell.AccountCell
import me.ewan.cellit.cell.Cell
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
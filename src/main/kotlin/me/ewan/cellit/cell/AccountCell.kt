package me.ewan.cellit.cell

import me.ewan.cellit.account.Account
import javax.persistence.*

@Entity
data class AccountCell (
        @Id @GeneratedValue
        var accountCellId : Long? = null,

        @ManyToOne
        @JoinColumn(name = "accountId")
        var account: Account,

        @ManyToOne
        @JoinColumn(name = "cellId")
        var cell: Cell
)
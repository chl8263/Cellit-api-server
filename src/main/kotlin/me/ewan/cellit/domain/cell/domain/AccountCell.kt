package me.ewan.cellit.domain.cell.domain

import me.ewan.cellit.domain.account.domain.Account
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
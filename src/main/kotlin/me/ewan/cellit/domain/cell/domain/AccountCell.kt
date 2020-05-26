package me.ewan.cellit.domain.cell.domain

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.cell.model.AccountCellRole
import javax.persistence.*

@Entity
class AccountCell (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountCellId : Long? = null,

        @Enumerated(EnumType.STRING)
        var accountCellRole : AccountCellRole? = AccountCellRole.USER,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "accountId")
        var account: Account,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cellId")
        var cell: Cell
)
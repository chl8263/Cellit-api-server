package me.ewan.cellit.domain.account.domain

import me.ewan.cellit.domain.cell.domain.AccountCell
import org.codehaus.jackson.annotate.JsonIgnore
import javax.persistence.*

@Entity
class Account (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountId: Long? = null,

        @Column(unique = true)
        var accountname: String,

        var password: String,

        //@ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        var role: AccountRole = AccountRole.USER,

        @JsonIgnore
        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
        var accountCells: MutableList<AccountCell> = mutableListOf()
)
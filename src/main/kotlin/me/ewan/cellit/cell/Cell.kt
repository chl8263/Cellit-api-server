package me.ewan.cellit.cell

import me.ewan.cellit.account.Account
import javax.persistence.*

@Entity
data class Cell(
       @Id @GeneratedValue
       var cellId: Long? = null,
       var cellName: String,

       @OneToMany(mappedBy = "cell")
       var accounts: MutableSet<AccountCell> = mutableSetOf()
)

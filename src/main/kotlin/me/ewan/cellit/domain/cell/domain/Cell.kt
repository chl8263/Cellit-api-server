package me.ewan.cellit.domain.cell.domain

import javax.persistence.*

@Entity
class Cell(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
       var cellId: Long? = null,

       var cellName: String,

       @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY)
       var accounts: MutableList<AccountCell> = mutableListOf()
)

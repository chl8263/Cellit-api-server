package me.ewan.cellit.domain.cell.vo.domain

import me.ewan.cellit.domain.account.vo.domain.Account
import javax.persistence.*

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "accountCellId")
class AccountCell (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountCellId : Long? = null,

        @Enumerated(EnumType.STRING)
        var accountCellRole : AccountCellRole? = AccountCellRole.USER,

        // default fetch type = EAGER
        @ManyToOne
        @JoinColumn(name = "accountId")
        var account: Account,

        // default fetch type = EAGER
        @ManyToOne
        @JoinColumn(name = "cellId")
        var cell: Cell

)
//{
//        override fun toString() = kotlinToString(properties = toStringProperties)
//
//        override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
//
//        override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)
//
//
//        companion object {
//                private val equalsAndHashCodeProperties = arrayOf(AccountCell::accountCellId)
//                private val toStringProperties = arrayOf(
//                        AccountCell::accountCellId
//                )
//        }
//}
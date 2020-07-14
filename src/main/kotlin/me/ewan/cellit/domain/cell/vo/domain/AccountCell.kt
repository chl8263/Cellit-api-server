package me.ewan.cellit.domain.cell.vo.domain

import me.ewan.cellit.domain.account.vo.domain.Account
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "accountCellId")
class AccountCell (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountCellId : Long? = null,

        @Column
        @Enumerated(EnumType.STRING)
        var cellRole : CellRole = CellRole.USER,

        // default fetch type = EAGER
        @ManyToOne
        @JoinColumn(name = "accountId")
        var account: Account,

        // default fetch type = EAGER
        @ManyToOne
        @JoinColumn(name = "cellId")
        var cell: Cell,

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        @Column
        var active: Int = 1

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
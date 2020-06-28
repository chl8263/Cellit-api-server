package me.ewan.cellit.domain.account.vo.domain

import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import org.codehaus.jackson.annotate.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*


@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "accountId")
class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountId: Long? = null,

        @Column(unique = true)
        var accountname: String,

        @Column
        var password: String,

        @Column
        @Enumerated(EnumType.STRING)
        var role: AccountRole = AccountRole.ROLE_USER,

        // default fetch type = LAZY
        @JsonIgnore // prevent infinity loop when trans JSON
        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
        var accountCells: MutableList<AccountCell> = mutableListOf(),

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var modifyDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date()),

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
//                private val equalsAndHashCodeProperties = arrayOf(Account::accountId)
//                private val toStringProperties = arrayOf(
//                        Account::accountId,
//                        Account::accountname
//                )
//        }
//}
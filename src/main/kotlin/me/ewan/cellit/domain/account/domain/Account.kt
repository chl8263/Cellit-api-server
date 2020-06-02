package me.ewan.cellit.domain.account.domain

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import me.ewan.cellit.domain.cell.domain.AccountCell
import org.codehaus.jackson.annotate.JsonIgnore
import javax.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "accountId")
class Account (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountId: Long? = null,

        @Column(unique = true)
        var accountname: String,

        var password: String,

        @Enumerated(EnumType.STRING)
        var role: AccountRole = AccountRole.ROLE_USER,

        // default fetch type = LAZY
        @JsonIgnore
        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
        var accountCells: MutableList<AccountCell> = mutableListOf()
){
        override fun toString() = kotlinToString(properties = toStringProperties)

        override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)

        override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)


        companion object {
                private val equalsAndHashCodeProperties = arrayOf(Account::accountId)
                private val toStringProperties = arrayOf(
                        Account::accountId,
                        Account::accountname
                )
        }
}
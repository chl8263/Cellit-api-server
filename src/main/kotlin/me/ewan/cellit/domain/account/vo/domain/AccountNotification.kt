package me.ewan.cellit.domain.account.vo.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*


@Entity
class AccountNotification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var accountNotificationId: Long? = null,

        @Column
        var message: String = "",

        @Column
        var status: String = AccountNotificationStatus.APPROVE,

        @ManyToOne
        @JoinColumn(name = "accountId")
        var account: Account,

        //@CreationTimestamp
        @Column
        @Temporal(TemporalType.TIMESTAMP)
        var createDate: String = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
)

object AccountNotificationStatus {
        const val APPROVE = "APPROVE"
        const val UPDATE = "UPDATE"
        const val REJECT = "REJECT"
}
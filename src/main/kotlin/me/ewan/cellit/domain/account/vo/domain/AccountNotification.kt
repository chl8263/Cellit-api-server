/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.domain.account.vo.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.Temporal
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

/**
 * AccountNotification domain.
 *
 * <p>
 *      AccountNotification domain is for notice message to Account.
 *
 *     * Don't use data class because the hashCode, toString that automatically made from data class cause infinity problem. *
 * </p>
 * @author Ewan
 */
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
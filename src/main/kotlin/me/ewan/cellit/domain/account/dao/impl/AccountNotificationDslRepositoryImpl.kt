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

package me.ewan.cellit.domain.account.dao.impl

import com.querydsl.core.BooleanBuilder
import me.ewan.cellit.domain.account.dao.AccountDslRepository
import me.ewan.cellit.domain.account.dao.AccountNotificationDslRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import me.ewan.cellit.domain.account.vo.domain.QAccount
import me.ewan.cellit.domain.account.vo.domain.QAccountNotification
import me.ewan.cellit.domain.account.vo.query.AccountNotificationQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

/**
 * @author Ewan
 */
class AccountNotificationDslRepositoryImpl : QuerydslRepositorySupport(AccountNotification::class.java), AccountNotificationDslRepository {

    /**
     * Get Notification list with dynamic retrieve query.
     *
     * @author Ewan
     * @param accountId
     * @param accountNotificationQuery query for retrieve
     * @return
     */
    override fun findAccountNotificationWithQuery(accountId: Long, accountNotificationQuery: AccountNotificationQuery): List<AccountNotification> {
        val accountNotification = QAccountNotification.accountNotification
        val account = QAccount.account

        val query = from(accountNotification)
        val builder = BooleanBuilder()

        builder.and(accountNotification.account.accountId.eq(accountId))

        if(accountNotificationQuery.accountNotificationId != null){
            builder.and(accountNotification.accountNotificationId.eq(accountNotificationQuery.accountNotificationId))
        }
        if(accountNotificationQuery.message != null){
            builder.and(accountNotification.message.like("%${accountNotificationQuery.message}%"))
        }
        query.where(builder)
                .orderBy(accountNotification.createDate.desc())

        if(accountNotificationQuery.offset != null && accountNotificationQuery.limit != null){
            query.offset(accountNotificationQuery.offset!!)
            query.limit(accountNotificationQuery.limit!!)
        }

        return query.fetch()
    }
}
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

class AccountNotificationDslRepositoryImpl : QuerydslRepositorySupport(AccountNotification::class.java), AccountNotificationDslRepository {

    override fun findAccountNotificationWithQuery(accountId: Long, accountNotificationQuery: AccountNotificationQuery): List<AccountNotification> {
        val accountNotification = QAccountNotification.accountNotification
        val account = QAccount.account

        val query = from(accountNotification)
        val builder = BooleanBuilder()

        builder.and(accountNotification.account.accountId.eq(account.accountId))

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
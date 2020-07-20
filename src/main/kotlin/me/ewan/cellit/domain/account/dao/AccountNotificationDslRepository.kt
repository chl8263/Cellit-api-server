package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import me.ewan.cellit.domain.account.vo.query.AccountNotificationQuery

interface AccountNotificationDslRepository {
    fun findAccountNotificationWithQuery(accountNotificationQuery: AccountNotificationQuery): List<AccountNotification>
}
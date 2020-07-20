package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import org.springframework.data.jpa.repository.JpaRepository

interface AccountNotificationRepository : JpaRepository<AccountNotification, Long>, AccountNotificationDslRepository {

}

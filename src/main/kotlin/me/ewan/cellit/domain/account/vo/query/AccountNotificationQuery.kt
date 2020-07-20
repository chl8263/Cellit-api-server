package me.ewan.cellit.domain.account.vo.query

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.global.common.Query
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountNotificationQuery(
        var accountNotificationId: Long? = null,

        var message: String? = null,

        var accountId: Long? = null,

        var createDate: String? = null,

        override var offset: Int? = null,

        override var limit: Int? = null
) : Query()
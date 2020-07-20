package me.ewan.cellit.domain.account.vo.dto

import me.ewan.cellit.domain.account.vo.domain.AccountNotificationStatus
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountNotificationDto (
        @NotEmpty
        @NotNull
        var message: String= "",

        @NotEmpty
        @NotNull
        var status: String = AccountNotificationStatus.APPROVE
)
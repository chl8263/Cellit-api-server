package me.ewan.cellit.domain.account.vo.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountNotificationDto (
        @NotEmpty
        @NotNull
        var message: String= ""
)
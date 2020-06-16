package me.ewan.cellit.domain.account.vo.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class AccountDto (

        @NotEmpty
        @NotNull
        var accountname: String? = null,
        @NotEmpty
        @NotNull
        var password: String? = null
        )
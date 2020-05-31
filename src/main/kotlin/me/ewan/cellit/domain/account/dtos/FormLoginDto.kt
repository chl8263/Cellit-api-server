package me.ewan.cellit.domain.account.dtos

import org.codehaus.jackson.annotate.JsonProperty

data class FormLoginDto(
        @field:JsonProperty("accountname")
        val accountname: String? = null,

        @field:JsonProperty("password")
        val password: String? = null
)
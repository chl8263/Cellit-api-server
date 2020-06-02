package me.ewan.cellit.global.security.dtos

import org.codehaus.jackson.annotate.JsonProperty

data class JwtAuthenticationDto(
        @field:JsonProperty("accountname")
        val accountname: String? = null,

        @field:JsonProperty("password")
        val password: String? = null
)
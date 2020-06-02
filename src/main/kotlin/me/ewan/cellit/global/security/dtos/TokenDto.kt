package me.ewan.cellit.global.security.dtos

import org.codehaus.jackson.annotate.JsonProperty

data class TokenDto(
        @field:JsonProperty("token")
        var token: String? = null
)
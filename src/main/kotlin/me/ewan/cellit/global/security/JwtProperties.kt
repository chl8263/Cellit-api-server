package me.ewan.cellit.global.security

object JwtProperties {
    const val ISSUER: String = "cellit"
    const val USER_NAME: String = "USER_NAME"
    const val USER_ROLE: String = "USER_ROLE"
    const val SECRET: String = "CELLIT_API"
    const val EXPIRATION_TIME: Int = 864000000 // 10 days
    const val BEARER_PREFIX: String = "Bearer"
    const val HEADER_STRING: String = "Authorization"
}
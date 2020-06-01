package me.ewan.cellit.global.security

object JwtProperties {
    val ISSUER: String = "cellit"
    val USER_NAME: String = "USER_NAME"
    val USER_ROLE: String = "USER_ROLE"
    val SECRET: String = "CELLIT_API"
    val EXPIRATION_TIME: Int = 864000000 // 10 days
    val TOKEN_PREFIX: String = "Bearer"
    val HEADER_STRING: String = "Authorization"
}
package me.ewan.cellit.domain.account.domain

enum class AccountRole {
    ROLE_ADMIN, ROLE_USER;

    companion object {
        fun getRoleByName(role: String) : AccountRole =
                values().find { x -> x.name == "ROLE_$role" } ?: throw NoSuchElementException()
    }
}
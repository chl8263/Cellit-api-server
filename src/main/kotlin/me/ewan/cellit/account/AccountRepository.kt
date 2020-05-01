package me.ewan.cellit.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String?) : Account
}
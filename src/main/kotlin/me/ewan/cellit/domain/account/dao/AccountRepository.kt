package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUsername(username: String?) : Account
}
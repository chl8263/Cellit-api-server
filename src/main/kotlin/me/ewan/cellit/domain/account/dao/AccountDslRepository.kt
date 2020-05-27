package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.domain.Account

interface AccountDslRepository {
    fun findAccountFetch(accountId: Long): Account
}
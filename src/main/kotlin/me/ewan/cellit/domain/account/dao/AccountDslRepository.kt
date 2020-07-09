package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.vo.domain.Account

interface AccountDslRepository {
    fun findAccountFetch(accountId: Long): Account?
}
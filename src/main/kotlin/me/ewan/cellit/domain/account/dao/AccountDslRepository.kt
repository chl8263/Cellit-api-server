package me.ewan.cellit.domain.account.dao

import me.ewan.cellit.domain.account.domain.Account

interface AccountDslRepository {
    fun findAllAccountData(): Account
}
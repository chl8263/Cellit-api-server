/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.domain.account.dao.impl

import me.ewan.cellit.domain.account.dao.AccountDslRepository
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.QAccount
import me.ewan.cellit.domain.cell.vo.domain.QAccountCell
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AccountDslRepositoryImpl() : QuerydslRepositorySupport(Account::class.java), AccountDslRepository{

    override fun findAccountFetch(accountId: Long): Account? {
        val account = QAccount.account
        val accountCell = QAccountCell.accountCell
        // @Query("SELECT a FROM Account a  JOIN FETCH a.accountCells WHERE a.accountId = :accountId")
        return from(account)
                .leftJoin(account.accountCells, accountCell).fetchJoin()
                .where(account.accountId.eq(accountId)
                        .and(accountCell.cell.active.eq(1)))
                .fetchOne()
    }
}
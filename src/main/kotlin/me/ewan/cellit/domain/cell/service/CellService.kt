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

package me.ewan.cellit.domain.cell.service

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import me.ewan.cellit.domain.channel.dao.ChannelRepository
import me.ewan.cellit.domain.channel.vo.domain.Channel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Ewan
 */
@Service
class CellService {
    @Autowired
    lateinit var cellRepository: CellRepository

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var accountCellRepository: AccountCellRepository

    @Autowired
    lateinit var channelRepository: ChannelRepository

    @Autowired
    lateinit var cellRequestRepository: CellRequestRepository

    /**
     * Get Cell by cell id
     *
     * @author Ewan
     * @param cellId
     * @return a Cell of matching with Cell id
     */
    fun getCellWithId(cellId: Long): Cell? = cellRepository.getOne(cellId)

    /**
     * Get Cell by cellName.
     *
     * @author Ewan
     * @param cellName
     * @return a Cell of matching with CellName.
     */
    fun getCellWithName(cellName: String): Cell? = cellRepository.findByCellName(cellName)

    /**
     * Create Cell by cell cellDto, cellName.
     *
     * @author Ewan
     * @param cellDto
     * @param cellName
     * @return a CellRequest of matching with CellDto, cellName
     */
    fun createCell(cellDto: CellDto, cellName: String): Cell {

        val currentUser = accountService.getAccountByName(cellName) ?: throw NoSuchElementException("Cannot find this account for create cell")

        val cell = Cell(cellName = cellDto.cellName!!.trim(), cellDescription = cellDto.cellDescription)
        val savedCell = cellRepository.save(cell)
        val accountCell = AccountCell(cellRole = CellRole.CREATOR, account = currentUser, cell = savedCell)

        val defaultChannel = Channel(channelName = "common", cell = savedCell)
        channelRepository.save(defaultChannel)

        accountCellRepository.save(accountCell)

        return savedCell
    }

    /**
     * Delete account from specific Cell.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return a number of result
     */
    fun deleteAccountAtCell(cellId: Long, accountId: Long): Long {
        return cellRepository.deleteAccount(cellId, accountId)
    }

    /**
     * Insert account in specific Cell.
     *
     * @author Ewan
     * @param
     * @param account
     * @param cell
     * @return an AccountCell of inserted account
     */
    fun insertAccountAtCell(account: Account, cell: Cell): AccountCell {
        val accountCell = AccountCell(cellRole = CellRole.USER, account = account, cell = cell)
        accountCellRepository.save(accountCell)
        return accountCell
    }

    /**
     * Get Cell list by query.
     *
     * @author Ewan
     * @param cellQuery
     * @return a Cell list of matching with Cell query
     */
    fun getCellsWithQuery(cellQuery: CellQuery): List<Cell> {
        val cells = cellRepository.findCellsWithQuery(cellQuery)
        return cells
    }

    /**
     * Get Account from specific Cell.
     *
     * @author Ewan
     * @param cellId
     * @param accountId
     * @return an Account of matching with Cell id, account id
     */
    fun findAccountInCell(cellId: Long, accountId: Long): Account? {
        val account = cellRepository.findAccountInCell(cellId, accountId)
        return account
    }

    /**
     * Get Account list by cellId.
     *
     * @author Ewan
     * @param cellId
     * @return an Account list of matching with CellId
     */
    fun findAccountsInCell(cellId: Long): List<Account> {
        val accounts = cellRepository.findAccounts(cellId)
        return accounts
    }
}
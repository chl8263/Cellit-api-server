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

    fun getCellWithId(cellId: Long): Cell? = cellRepository.getOne(cellId)
    fun getCellWithName(cellName: String): Cell? = cellRepository.findByCellName(cellName)

    fun createCell(cellDto: CellDto, name: String): Cell {

        val currentUser = accountService.getAccountWithName(name) ?: throw NoSuchElementException("Cannot find this account for create cell")

        val cell = Cell(cellName = cellDto.cellName!!.trim(), cellDescription = cellDto.cellDescription)
        //val cell = modelMapper.map(cellDto, Cell::class.java)
        val savedCell = cellRepository.save(cell)
        val accountCell = AccountCell(cellRole = CellRole.CREATOR, account = currentUser, cell = savedCell)

        val defaultChannel = Channel(channelName = "common", cell = savedCell)
        channelRepository.save(defaultChannel)

        accountCellRepository.save(accountCell)

        return savedCell
    }

    fun insertAccountAtCell(foundAccount: Account, foundCell: Cell): AccountCell {
        val accountCell = AccountCell(cellRole = CellRole.USER, account = foundAccount, cell = foundCell)
        accountCellRepository.save(accountCell)
        return accountCell
    }

    fun getCellsWithQuery(cellQuery: CellQuery): List<Cell> {
        val cells = cellRepository.findCellsWithQuery(cellQuery)
        return cells
    }

    fun findAccountInCell(cellId: Long, accountId: Long): Account? {
        val account = cellRepository.findAccountInCell(cellId, accountId)
        return account
    }
}
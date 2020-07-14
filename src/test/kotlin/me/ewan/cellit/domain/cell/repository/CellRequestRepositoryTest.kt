package me.ewan.cellit.domain.cell.repository

import me.ewan.cellit.common.BaseControllerTest
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.dao.CellRequestRepository
import me.ewan.cellit.domain.cell.service.CellRequestService
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

class CellRequestRepositoryTest : BaseControllerTest(){

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var cellService: CellService

    @Autowired
    private lateinit var cellRepository: CellRepository

    @Autowired
    private lateinit var accountCellRepository: AccountCellRepository

    @Autowired
    private lateinit var cellRequestService: CellRequestService

    @Autowired
    private lateinit var cellRequestRepository: CellRequestRepository

    @Test
    fun deleteCellRequestsWithCellIdAndAccountId(){
        //given
        val name = appProperties.testUserAccountname
        val pw = appProperties.testUserPassword
        val name2 = appProperties.testUserAccountname2
        val pw2 = appProperties.testUserPassword2
        val testAccount1 =createAccount(name = name, pw = pw)
        val testAccount2 = createAccount(name = name2, pw = pw2)

        val cellName1 = "Cell_test1"
        val cellDto1 = CellDto(cellName = cellName1)
        val savedCell1 = cellService.createCell(cellDto1, testAccount1.accountname)

        val cellRequest = CellRequest(cell = savedCell1, accountId = testAccount2.accountId, accountName = testAccount2.accountname)
        cellRequestService.createCellRequest(cellRequest)

        //when
        val result = cellRequestRepository.deleteCellRequestsWithCellIdAndAccountId(savedCell1.cellId!!, testAccount2.accountId!!)

        //then
        Assertions.assertThat(result).isEqualTo(1L)
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.ROLE_USER): Account {
        var account = Account(accountname = name, password = pw, role = role)

        var savedAccount = accountService.createAccount(account)
        return savedAccount
    }

    private fun createCell(name: String): Cell {
        var cell = Cell(cellName = name)

        var savedCell = cellRepository.save(cell)
        return savedCell
    }
}

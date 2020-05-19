package me.ewan.cellit.domain.cell

import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.dao.CellRepository
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.domain.Cell
import me.ewan.cellit.domain.common.BaseControllerTest
import me.ewan.cellit.global.common.AppProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.assertj.core.api.Assertions.assertThat

class CellJpaTest : BaseControllerTest(){

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var cellRepository: CellRepository

    @Autowired
    private lateinit var accountCellRepository: AccountCellRepository

    @BeforeEach
    fun setUp(){
//        accountRepository.deleteAll()
//
//        val username = appProperties.testUserUsername
//        val password = appProperties.testUserPassword
//        createAccount(name = username, pw = password)
    }

    @Test
    fun `save account cell`(){
        //given
        val userName = appProperties.testUserAccountname
        val userPw = appProperties.testUserPassword
        val savedAccount = createAccount(name = userName, pw = userPw)

        val cellName = "IT team"
        val savedCell = createCell(name = cellName)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell)

        //when
        val savedAccountCell = accountCellRepository.save(accountCell)

        //then
        assertThat(savedAccountCell.account).isEqualTo(savedAccount)
        assertThat(savedAccountCell.cell).isEqualTo(savedCell)
    }

    @Test
    fun `get account cell with account id`(){
        //given
        val accountname = appProperties.testUserAccountname
        val userPw = appProperties.testUserPassword
        val savedAccount = createAccount(name = accountname, pw = userPw)

        val cellName = "IT team"
        val savedCell = createCell(name = cellName)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell)
        val savedAccountCell = accountCellRepository.save(accountCell)

        //when
        val reCallAccount = accountRepository.findByAccountname(accountname)

        //then
        assertThat(savedAccount.accountId).isEqualTo(reCallAccount.accountCells[0].account.accountId)
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.USER): Account {
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
package me.ewan.cellit.domain.account

import me.ewan.cellit.TestConfiguration
import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.dao.AccountRepository
import me.ewan.cellit.domain.account.domain.AccountRole
import me.ewan.cellit.domain.cell.domain.AccountCell
import me.ewan.cellit.domain.cell.dao.AccountCellRepository
import me.ewan.cellit.domain.cell.domain.Cell
import me.ewan.cellit.domain.cell.dao.CellRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

import org.assertj.core.api.Assertions.assertThat
import org.springframework.context.annotation.Import

@ExtendWith(SpringExtension::class)
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var cellRepository: CellRepository

    @Autowired
    lateinit var accountCellRepository: AccountCellRepository

    @Test
    fun createAccount(){
        //given
        val accountname = "ewan"
        val pw = "123"
        val account = createAccount(accountname, pw)

        //then
        assertThat(account.accountname).isEqualTo(accountname)
    }

    @Test
    fun accountWithCell(){
        //given
        val cellName = "workout"
        val savedCell = createCell(name = cellName)

        val accountname = "ewan"
        val userPw = "123"
        val savedAccount = createAccount(name = accountname, pw = userPw)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell)
        val savedAccountCell = accountCellRepository.saveAndFlush(accountCell)

        //then
        assertThat(savedAccount.accountname).isEqualTo(accountname)
        assertThat(savedAccountCell.account).isEqualTo(savedAccount)
        assertThat(savedAccountCell.cell).isEqualTo(savedCell)
    }

    private fun createAccount(name: String, pw: String, role: AccountRole = AccountRole.USER): Account {
        var account = Account(accountname = name, password = pw, role = role)

        var savedAccount =  accountRepository.save(account)
        return savedAccount
    }

    private fun createCell(name: String): Cell {
        var cell = Cell(cellName = name)

        var savedCell =  cellRepository.save(cell)
        return savedCell
    }

}
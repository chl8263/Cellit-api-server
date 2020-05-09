package me.ewan.cellit.account

import me.ewan.cellit.TestConfiguration
import me.ewan.cellit.cell.AccountCell
import me.ewan.cellit.cell.AccountCellRepository
import me.ewan.cellit.cell.Cell
import me.ewan.cellit.cell.CellRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

import org.assertj.core.api.Assertions.assertThat
import org.springframework.context.annotation.Import

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Import(TestConfiguration::class)
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
        val username = "ewan"
        val pw = "123"
        val account = createAccount(username, pw)

        //then
        assertThat(account.username).isEqualTo(username)
    }

    @Test
    fun accountWithCell(){
        //given
        val cellName = "workout"
        val savedCell = createCell(name = cellName)

        val username = "ewan"
        val userPw = "123"
        val savedAccount = createAccount(name = username, pw = userPw)

        val accountCell = AccountCell(account = savedAccount, cell = savedCell)
        val savedAccountCell = accountCellRepository.saveAndFlush(accountCell)

        //then
        assertThat(savedAccount.username).isEqualTo(username)
        assertThat(savedAccountCell.account).isEqualTo(savedAccount)
        assertThat(savedAccountCell.cell).isEqualTo(savedCell)
    }

    private fun createAccount(name: String, pw: String, role: String = "USER"): Account{
        var account = Account(username = name, password = pw, role = role)

        var savedAccount =  accountRepository.save(account)
        return savedAccount
    }

    private fun createCell(name: String): Cell{
        var cell = Cell(cellName = name)

        var savedCell =  cellRepository.save(cell)
        return savedCell
    }

}
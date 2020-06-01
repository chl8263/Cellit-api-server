package me.ewan.cellit.domain.account.service

import me.ewan.cellit.domain.account.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Autowired
    lateinit var accountService: AccountService

    @Test
    fun findByUsername(){
        //given
        val account = Account(accountname = "ewan2", password = "123")
        accountService.createAccount(account)

        //when
        val userDetailsService: UserDetailsService = accountService
        val userDetails = userDetailsService.loadUserByUsername("ewan2")

        //then
        assertThat(userDetails.password).isEqualTo(account.password)
    }
}
package me.ewan.cellit.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/signUp")
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

    @PostMapping
    fun signUp(@ModelAttribute account: Account) : Account{
        val savedAccount = accountService.createAccount(account)
        return savedAccount
    }
}
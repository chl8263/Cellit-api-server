package me.ewan.cellit.account

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/signUp")
class SignUpController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping
    fun signUpForm() : String = "/signUp"

    @PostMapping
    fun Account(@ModelAttribute account: Account) : String{
        val savedAccount = accountService.createAccount(account)
        return "redirect:/"
    }
}
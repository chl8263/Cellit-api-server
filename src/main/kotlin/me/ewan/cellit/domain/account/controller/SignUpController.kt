package me.ewan.cellit.domain.account.controller

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/signUp")
class SignUpController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping
    fun signUpForm() : String = "/common/signUp"
}
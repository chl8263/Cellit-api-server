package me.ewan.cellit.domain.cell.controller

import me.ewan.cellit.domain.account.service.AccountService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.security.Principal

@Controller
@RequestMapping("/")
class MainBoardController {

    companion object : KLogging()

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    private lateinit var passWordEncoder: PasswordEncoder

    @GetMapping
    fun mainBoardForm(principal: Principal, model: Model) : ModelAndView {

        val currentAccount = accountService.getAccount(principal.name)

        model.addAttribute("userId", currentAccount.accountId)
        model.addAttribute("username", currentAccount.accountname)
        model.addAttribute("password", currentAccount.password)

        return ModelAndView("/mainBoard")
    }
}
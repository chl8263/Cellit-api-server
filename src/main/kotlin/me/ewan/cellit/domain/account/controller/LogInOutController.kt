package me.ewan.cellit.domain.account.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class LogInOutController {

    @GetMapping("/login")
    fun login(request: HttpServletRequest) : String {
        return "/login"
    }

    @RequestMapping("/loginError")
    fun loginError(model: Model) : String = "/login"
}
package me.ewan.cellit.account

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LogInOutController {

    @GetMapping("/login")
    fun login() : String {
        println("login controller")
        return "/login"
    }
}
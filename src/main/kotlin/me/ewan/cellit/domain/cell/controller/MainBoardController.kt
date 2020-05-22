package me.ewan.cellit.domain.cell.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
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

    @GetMapping
    fun mainBoardForm(principal: Principal, model: Model) : ModelAndView {

        model.addAttribute("userName",principal.name)

        return ModelAndView("/mainBoard")
    }
}
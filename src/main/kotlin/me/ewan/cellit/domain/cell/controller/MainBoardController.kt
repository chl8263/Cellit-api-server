package me.ewan.cellit.domain.cell.controller

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/")
class MainBoardController {

    companion object : KLogging()

    @GetMapping
    fun mainBoardForm(principal: Principal) : String {
        if(principal == null){
            logger.info { "Main Board is null" }
        }else {
            logger.info { "Main Board ${principal.name}" }
        }

        return "/mainBoard"
    }
}
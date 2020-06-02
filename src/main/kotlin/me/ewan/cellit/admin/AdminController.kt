package me.ewan.cellit.admin

import org.springframework.hateoas.MediaTypes
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/admin"])
class AdminController {

    @GetMapping(value = ["/user"])
    fun admin() = "admin!!!!user"

    @GetMapping
    fun admin2() = "admin!!!!admin"
}
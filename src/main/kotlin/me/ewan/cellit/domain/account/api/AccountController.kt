package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.model.CellModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping("/{accountName}/cells")
    fun getCellsFromAccountId(@PathVariable accountName: String): ResponseEntity<CellModel> {
        println("~~~~~~~~")
        val cells = accountService.getCellsWithAccountName(accountName)
        println("~~~~~~~~"+accountName)
        //println("~~~~~~~~"+cells[0])
//
//        val cellModel = CellModel(cells[0])

        //return ResponseEntity.ok(cellModel)
        return ResponseEntity.ok().build()
    }

}
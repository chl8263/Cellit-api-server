package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.model.CellModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account", produces = [MediaTypes.HAL_JSON_VALUE])
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping("/{accountName}/cells")
    fun getCellsFromAccountId(@PathVariable accountName: String): ResponseEntity<CellModel> {

        val cells = accountService.getCellsWithAccountName(accountName)

        val cellModel = CellModel(cells)

        return ResponseEntity.ok(cellModel)
    }
}
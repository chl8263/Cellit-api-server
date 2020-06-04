package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.model.CellRepresentModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/account", produces = [MediaTypes.HAL_JSON_VALUE])
class AccountController{

    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping
    fun createNewAccount(@ModelAttribute account: Account){
        accountService.createAccount(account)
    }


    @GetMapping("/{accountId}/cells")
    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<CollectionModel<CellRepresentModel>> {

        val cells = accountService.getCellsWithAccountId(accountId)

        val entityModel = cells.map {
            val cellModel = CellRepresentModel(it)
            val selfLink = linkTo(CellController::class.java).slash(it.cellId)
                    .withSelfRel()
            cellModel.add(selfLink)
        }

        val selfLink = linkTo(methodOn(AccountController::class.java).getCellsFromAccountId(accountId)).withSelfRel()

        val resultEntityModel = CollectionModel(entityModel, selfLink)

        return ResponseEntity.ok(resultEntityModel)
    }
}
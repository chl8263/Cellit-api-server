package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.account.vo.model.AccountModel
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.model.CellModel
import org.modelmapper.ModelMapper
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
    fun createNewAccount(@RequestBody accountDto: AccountDto): ResponseEntity<AccountModel>{

        val account = Account(accountname = accountDto.accountname!!, password = accountDto.password!!, role = AccountRole.ROLE_USER)
        //account.role = AccountRole.ROLE_USER
        val savedAccount = accountService.createAccount(account)

        val accountModel = savedAccount.let {
            val accountModel = AccountModel(it)
            val selfLink = linkTo(methodOn(AccountController::class.java).createNewAccount(accountDto)).withSelfRel()
            accountModel.add(selfLink)
        }

        val linkBuilder = linkTo(AccountController::class.java).slash(savedAccount.accountId)
        val createdUri = linkBuilder.toUri()

        return ResponseEntity.created(createdUri).body(accountModel)
    }


    @GetMapping("/{accountId}/cells")
    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<CollectionModel<CellModel>> {

        val cells = accountService.getCellsWithAccountId(accountId)

        val entityModel = cells.map {
            val cellModel = CellModel(it)
            val selfLink = linkTo(CellController::class.java).slash(it.cellId)
                    .withSelfRel()
            cellModel.add(selfLink)
        }

        val selfLink = linkTo(methodOn(AccountController::class.java).getCellsFromAccountId(accountId)).withSelfRel()

        val resultEntityModel = CollectionModel(entityModel, selfLink)

        return ResponseEntity.ok(resultEntityModel)
    }
}
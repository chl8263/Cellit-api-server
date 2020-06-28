package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.account.vo.dto.validator.AccountDtoValidator
import me.ewan.cellit.domain.account.vo.entityModel.AccountEntityModel
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.global.common.ErrorToJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/account"], produces = [MediaTypes.HAL_JSON_VALUE])
class AccountController{

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountDtoValidator: AccountDtoValidator

    @Autowired
    private lateinit var errorToJson: ErrorToJson

    @PostMapping
    fun createAccount(@RequestBody @Valid accountDto: AccountDto, errors: Errors): ResponseEntity<Any>{
//        if(errors.hasErrors()){
//            return ResponseEntity.badRequest().build()
//        }
        accountDtoValidator.validate(accountDto, errors)
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errorToJson.convert(errors))
        }

        val account = Account(accountname = accountDto.accountname!!, password = accountDto.password!!, role = AccountRole.ROLE_USER)
        //account.role = AccountRole.ROLE_USER
        val savedAccount = accountService.createAccount(account)

        val accountEntityModel = savedAccount.run {
            val accountModel = AccountEntityModel(this)
            val selfLink = linkTo(AccountController::class.java).slash(savedAccount.accountId).withSelfRel()
            accountModel.add(selfLink)
        }

        val linkBuilder = linkTo(AccountController::class.java)
        val createdUri = linkBuilder.toUri()

        return ResponseEntity.created(createdUri).body(accountEntityModel)
    }

    @GetMapping("/{accountName}")
    fun getAccountWithUserName(@PathVariable accountName: String): ResponseEntity<Any>{

        val account = accountService.getAccount(accountName)

        val accountEntityModel = account.run {
            val accountModel = AccountEntityModel(this)
            val selfLink = linkTo(AccountController::class.java).slash(account.accountId).withSelfRel()
            accountModel.add(selfLink)
        }

        return ResponseEntity.ok(accountEntityModel)
    }


    @GetMapping("/{accountId}/cells")
    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<CollectionModel<CellEntityModel>> {

        val accountCells = accountService.getAccountCellsWithAccountId(accountId)

        val cellsEntityModel = accountCells.map {
            val tempCellDto = CellDto(cellId = it.cell.cellId, cellName = it.cell.cellName)
            val cellModel = CellEntityModel(tempCellDto, it.cellRole.name)
            val selfLink = linkTo(CellController::class.java).slash(it.cell.cellId)
                    .withSelfRel()
            cellModel.add(selfLink)
        }

        val selfLink = linkTo(methodOn(AccountController::class.java).getCellsFromAccountId(accountId)).withSelfRel()
        val resultEntityModel = CollectionModel(cellsEntityModel, selfLink)

        return ResponseEntity.ok(resultEntityModel)
    }
}
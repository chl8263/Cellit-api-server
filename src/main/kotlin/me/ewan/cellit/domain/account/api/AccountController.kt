package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.account.vo.dto.validator.AccountDtoValidator
import me.ewan.cellit.domain.account.vo.entityModel.AccountEntityModel
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.domain.AccountCell
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.global.error.ErrorHelper
import me.ewan.cellit.global.error.ErrorToJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.ServletWebRequest
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/accounts"], produces = [MediaTypes.HAL_JSON_VALUE])
class AccountController{

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountDtoValidator: AccountDtoValidator

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    @PostMapping
    fun createAccount(@RequestBody @Valid accountDto: AccountDto,
                      request: ServletWebRequest): ResponseEntity<Any>{
        try {
            // s: validator
            val errorList = accountDtoValidator.validate(accountDto)
            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val account = Account(accountname = accountDto.accountname!!, password = accountDto.password!!, role = AccountRole.ROLE_USER)
            accountService.getAccountWithName(accountDto.accountname!!)?.let {
                val body = errorHelper.getUnexpectError("This account already exit, Please try another one.")
                return ResponseEntity.badRequest().body(body)
            }

            val savedAccount = accountService.createAccount(account)

            val accountEntityModel = savedAccount.run {
                val accountModel = AccountEntityModel(this)
                val selfLink = linkTo(AccountController::class.java).slash(savedAccount.accountId).withSelfRel()
                accountModel.add(selfLink)
            }

            val linkBuilder = linkTo(AccountController::class.java)
            val createdUri = linkBuilder.toUri()

            return ResponseEntity.created(createdUri).body(accountEntityModel)

        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @GetMapping("/{accountName}")
    fun getAccountWithUserName(@PathVariable accountName: String): ResponseEntity<Any>{
        try {
            val account = accountService.getAccountWithName(accountName)?.let {
                val accountModel = AccountEntityModel(it)
                val selfLink = linkTo(AccountController::class.java).slash(it.accountId).withSelfRel()
                accountModel.add(selfLink)
                return ResponseEntity.ok(accountModel)
            }
            val body = errorHelper.getUnexpectError("This account doesn't exist.")
            return ResponseEntity.badRequest().body(body)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }


    @GetMapping("/{accountId}/cells")
    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<Any> {
        try {
            val accountCells = accountService.getAccountCellsWithAccountId(accountId) ?: ArrayList<AccountCell>()

            val cellsEntityModel = accountCells.map {
                val tempCellDto = CellDto(cellId = it.cell.cellId, cellName = it.cell.cellName, cellDescription = it.cell.cellDescription, createDate = it.cell.createDate)
                val cellModel = CellEntityModel(tempCellDto, it.cellRole.name)
                val selfLink = linkTo(CellController::class.java).slash(it.cell.cellId)
                        .withSelfRel()
                cellModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(AccountController::class.java).getCellsFromAccountId(accountId)).withSelfRel()
            val resultEntityModel = CollectionModel(cellsEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }
}
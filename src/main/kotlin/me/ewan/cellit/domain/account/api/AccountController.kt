/********************************************************************************************
 * Copyright (c) 2020 Ewan Choi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************************/

package me.ewan.cellit.domain.account.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.domain.Account
import me.ewan.cellit.domain.account.vo.domain.AccountNotification
import me.ewan.cellit.domain.account.vo.domain.AccountRole
import me.ewan.cellit.domain.account.vo.dto.AccountDto
import me.ewan.cellit.domain.account.vo.dto.AccountNotificationDto
import me.ewan.cellit.domain.account.vo.dto.validator.AccountDtoValidator
import me.ewan.cellit.domain.account.vo.entityModel.AccountEntityModel
import me.ewan.cellit.domain.account.vo.entityModel.AccountNotificationEntityModel
import me.ewan.cellit.domain.account.vo.query.AccountNotificationQuery
import me.ewan.cellit.domain.cell.api.CellController
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.global.common.ConvertQueryToClass
import me.ewan.cellit.global.error.Const.UNEXPECTED_ERROR_WORD
import me.ewan.cellit.global.error.ErrorHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Ewan
 */
@RestController
@RequestMapping(value = ["/api/accounts"], produces = [MediaTypes.HAL_JSON_VALUE])
class AccountController{

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountDtoValidator: AccountDtoValidator

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    /**
     * Create new Account.
     *
     * @author Ewan
     * @param AccountDto account information
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PostMapping
    fun createAccount(@RequestBody @Valid accountDto: AccountDto): ResponseEntity<Any>{
        try {
            // s: validations
            val errorList = accountDtoValidator.validate(accountDto)
            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val account = Account(accountname = accountDto.accountname!!,
                    password = accountDto.password!!,
                    role = AccountRole.ROLE_USER)

            accountService.getAccountByName(accountDto.accountname!!)?.let {
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
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Get single user by account name.
     *
     * @author Ewan
     * @param accountName
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("/{accountName}")
    fun getAccountByAccountName(@PathVariable accountName: String): ResponseEntity<Any>{
        try {
            // s: validations
            val foundAccount = accountService.getAccountByName(accountName)
            if(foundAccount == null){
                val body = errorHelper.getUnexpectError("Not exits this account.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val entityModel = foundAccount?.let {
                val accountModel = AccountEntityModel(it)
                val selfLink = linkTo(AccountController::class.java).slash(it.accountId).withSelfRel()
                accountModel.add(selfLink)
            }

            return ResponseEntity.ok(entityModel)

        }catch (e: Exception){
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Get cell list by account id.
     *
     * @author Ewan
     * @param accountId
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("/{accountId}/cells")
    fun getCellsByAccountId(@PathVariable accountId: Long): ResponseEntity<Any> {
        try {
            // s: validations
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                val body = errorHelper.getUnexpectError("Not exits this account.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val accountCells = accountService.getAccountCellsByAccountId(accountId) ?: ArrayList()

            val cellsEntityModel = accountCells.map {
                val tempCellDto = CellDto(cellId = it.cell.cellId, cellName = it.cell.cellName, cellDescription = it.cell.cellDescription, createDate = it.cell.createDate)
                val cellModel = CellEntityModel(tempCellDto, it.cellRole.name)
                val selfLink = linkTo(CellController::class.java).slash(it.cell.cellId)
                        .withSelfRel()
                cellModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(AccountController::class.java).getCellsByAccountId(accountId)).withSelfRel()
            val resultEntityModel = CollectionModel(cellsEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)

        }catch (e: Exception){
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Get account notification list by account id, query.
     *
     * @author Ewan
     * @param accountId
     * @param query For retrieve as specific word
     * @param offset Start number of list
     * @param limit End number of list
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @GetMapping("/{accountId}/accountNotifications")
    fun getAccountNotifications(@PathVariable accountId: Long?,
                                @RequestParam query: String?,
                                @RequestParam offset: Int?,
                                @RequestParam limit: Int?): ResponseEntity<Any>{
        try{
            if(query == null){
                //TODO
            }else {

            }
            val convertedQuery = ConvertQueryToClass.convert<AccountNotificationQuery>(query, offset, limit)
            val accountNotifications = accountService.getAccountNotificationWithQuery(accountId!!, convertedQuery)

            val accountNotificationEntityModel = accountNotifications.map {
                val AccountNotificationModel = AccountNotificationEntityModel(it.accountNotificationId!!, it.account.accountId!!, it.message, it.status)
                val selfLink = linkTo(AccountController::class.java).slash(it.account.accountId!!).slash("accountNotifications").slash(it.accountNotificationId).withSelfRel()
                AccountNotificationModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(AccountController::class.java).getAccountNotifications(accountId, query, offset, limit)).withSelfRel()
            val resultEntityModel = CollectionModel(accountNotificationEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)
        }catch (e: java.lang.Exception){
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }

    /**
     * Create account notification.
     *
     * @author Ewan
     * @param accountId
     * @param accountNotificationDto account notification data
     * @return a ResponseEntity as satisfied with REST hateoas
     */
    @PostMapping("/{accountId}/accountNotifications")
    fun createAccountNotifications(@PathVariable accountId: Long,
                                   @RequestBody @Valid accountNotificationDto: AccountNotificationDto): ResponseEntity<Any> {
        try{
            // s: validations
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                val body = errorHelper.getUnexpectError("Not exits this account.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validations

            val accountNotification = AccountNotification(account = foundAccount, message = accountNotificationDto.message, status = accountNotificationDto.status)
            val savedAccountNotification = accountService.createAccountNotification(accountNotification)

            val accountNotificationEntityModel = savedAccountNotification.run {
                val accountNotificationModel = AccountNotificationEntityModel(savedAccountNotification.accountNotificationId!!, foundAccount.accountId!!, savedAccountNotification.message, savedAccountNotification.status)
                val selfLink = linkTo(AccountController::class.java).slash(foundAccount.accountId).slash("accountNotifications").slash(savedAccountNotification.accountNotificationId).withSelfRel()
                accountNotificationModel.add(selfLink)
            }

            val linkBuilder = linkTo(methodOn(AccountController::class.java).createAccountNotifications(accountId, accountNotificationDto)).withSelfRel()
            val createdUri = linkBuilder.toUri()

            return ResponseEntity.created(createdUri).body(accountNotificationEntityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError(UNEXPECTED_ERROR_WORD)
            return ResponseEntity.badRequest().body(body)
        }
    }
}
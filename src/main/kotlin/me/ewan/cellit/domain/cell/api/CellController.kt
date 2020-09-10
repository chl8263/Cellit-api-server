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

package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.api.AccountController
import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.account.vo.entityModel.AccountEntityModel
import me.ewan.cellit.domain.cell.service.CellRequestService
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import me.ewan.cellit.domain.cell.vo.dto.AccountCellDto
import me.ewan.cellit.domain.cell.vo.dto.CellRequestDto
import me.ewan.cellit.domain.cell.vo.dto.validator.CellDtoValidator
import me.ewan.cellit.domain.cell.vo.entityModel.AccountCellEntityModel
import me.ewan.cellit.domain.cell.vo.entityModel.CellRequestEntityModel
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import me.ewan.cellit.domain.channel.api.ChannelController
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import me.ewan.cellit.global.common.ConvertQueryToClass
import me.ewan.cellit.global.error.ErrorHelper
import me.ewan.cellit.global.error.vo.ErrorVo
import me.ewan.cellit.global.error.vo.HTTP_STATUS.BAD_REQUEST
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/cells"], produces = [MediaTypes.HAL_JSON_VALUE])
class CellController {

    @Autowired
    private lateinit var cellService: CellService

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var cellRequestService: CellRequestService

    @Autowired
    private lateinit var channelService: ChannelService

    @Autowired
    private lateinit var cellDtoValidator: CellDtoValidator

    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    @GetMapping
    fun getCells(@RequestParam query: String?,
                 @RequestParam offset: Int?,
                 @RequestParam limit: Int?): ResponseEntity<Any>{

        try {
            if (query == null) {
                // TODO
                println("11111")
                return ResponseEntity.badRequest().body("aaa")
            } else {
                val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query, offset, limit)
                val cells = cellService.getCellsWithQuery(convertedQuery as CellQuery)

                val cellsEntityModel = cells.map {
                    val tempCellDto = modelMapper.map(it, CellDto::class.java)
                    val cellModel = CellEntityModel(tempCellDto, "")
                    val selfLink = linkTo(CellController::class.java).slash(it.cellId).withSelfRel()
                    cellModel.add(selfLink)
                }

                val selfLink = linkTo(CellController::class.java).withSelfRel()
                val resultEntityModel = CollectionModel(cellsEntityModel, selfLink)

                return ResponseEntity.ok(resultEntityModel)
            }
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @PostMapping
    fun createCell(@RequestBody @Valid cellDto: CellDto): ResponseEntity<Any> {

        try {
            // s: validator
            val errorList = cellDtoValidator.validate(cellDto)
            if (errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }

            val auth = SecurityContextHolder.getContext().authentication
            cellService.getCellWithName(cellDto.cellName!!)?.let {
                val body = errorHelper.getUnexpectError("This cell already exit, Please try another one.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val savedCell = cellService.createCell(cellDto, auth.name)

            val entityModel = savedCell.run {
                val tempCellDto = modelMapper.map(savedCell, CellDto::class.java)
                val cellModel = CellEntityModel(tempCellDto, CellRole.CREATOR.name)
                val selfLink = linkTo(CellController::class.java).slash(this.cellId).withSelfRel()
                cellModel.add(selfLink)
            }

            val linkBuilder = linkTo(CellController::class.java)
            val createdUri = linkBuilder.toUri()

            return ResponseEntity.created(createdUri).body(entityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @GetMapping("/{cellId}/accounts")
    fun getAccountsAtCell(@PathVariable cellId: Long): ResponseEntity<Any> {
        try {
            // s: validator
            val errorList = ArrayList<ErrorVo>()
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "This Cell doesn't exits.", errorList)
            }

            if(errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val accountList = cellService.findAccountsInCell(foundCell!!.cellId!!)

            val entityModel = accountList.map {
                val cellModel = AccountEntityModel(it)
                val selfLink = linkTo(AccountController::class.java).slash(it.accountId).withSelfRel()
                cellModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(CellController::class.java).getAccountsAtCell(cellId)).withSelfRel()
            val resultEntityModel = CollectionModel(entityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)

        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @DeleteMapping("/{cellId}/accounts/{accountId}")
    fun deleteAccountAtCell(@PathVariable cellId: Long,
                            @PathVariable accountId: Long): ResponseEntity<Any> {
        try {
            // s: validator
            val errorList = ArrayList<ErrorVo>()
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "This Cell doesn't exits.", errorList)
            }
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "This Account doesn't exits.", errorList)
            }

            if(errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val savedAccountCell = cellService.deleteAccountAtCell(foundCell!!.cellId!!, foundAccount!!.accountId!!)

            if(savedAccountCell != 1L){
                val body = errorHelper.getUnexpectError("Cannot delete account at cell.")
                return ResponseEntity.badRequest().body(body)
            }

            val cellModel = AccountEntityModel(foundAccount)
            val selfLink = linkTo(AccountController::class.java).slash(foundAccount.accountId).withSelfRel()

            cellModel.add(selfLink)

            return ResponseEntity.ok(cellModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }


    @PostMapping("/{cellId}/accounts/{accountId}")
    fun insertAccountAtCell(@PathVariable cellId: Long,
                            @PathVariable accountId: Long): ResponseEntity<Any> {
        try {
            // s: validator
            val errorList = ArrayList<ErrorVo>()
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "This Cell doesn't exits.", errorList)
            }
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                errorHelper.addErrorAttributes(BAD_REQUEST, "This Account doesn't exits.", errorList)
            }

            if(errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val savedAccountCell = cellService.insertAccountAtCell(foundAccount!!, foundCell!!)

            val entityModel = savedAccountCell.run {
                val tempAccountCellDto = AccountCellDto(accountCellId = this.accountCellId,
                                                        accountId = accountId,
                                                        cellId = cellId,
                                                        createDate = this.createDate,
                                                        cellRole = this.cellRole)
                val cellModel = AccountCellEntityModel(tempAccountCellDto)
                val selfLink = linkTo(CellController::class.java).slash(cellId).slash("accountCells").withSelfRel()
                cellModel.add(selfLink)
            }

            val linkBuilder = linkTo(methodOn(CellController::class.java).insertAccountAtCell(cellId ,accountId)).withSelfRel()
            val createdUri = linkBuilder.toUri()

            return ResponseEntity.created(createdUri).body(entityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @GetMapping("/{cellId}/channels")
    fun getChannelsByCellId(@PathVariable cellId: Long): ResponseEntity<Any>{
        try {
            val channels = channelService.getChannelsByCellId(cellId)

            val channelsEntityModel = channels.sortedBy { x -> x.createDate }.map {
                val channelModel = ChannelEntityModel(modelMapper.map(it, ChannelDto::class.java))
                val selfLink = linkTo(ChannelController::class.java).slash(it.channelId).withSelfRel()
                channelModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(CellController::class.java).getChannelsByCellId(cellId)).withSelfRel()
            val resultEntityModel = CollectionModel(channelsEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @GetMapping("/{cellId}/cellRequests")
    fun getCellRequests(@PathVariable cellId: Long): ResponseEntity<Any>{
        try{
            // s: validator
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                val body = errorHelper.getUnexpectError("Not exits this cell.")
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val cellRequests = cellRequestService.getCellRequestsWithCell(foundCell.cellId!!)

            val cellsRequestEntityModel = cellRequests.map {
                val tempCellRequestDto = CellRequestDto(cellRequestId = it.cellRequestId, cellId = it.cell.cellId, accountId = it.accountId, accountName = it.accountName!!, createDate = it.createDate)
                val cellModel = CellRequestEntityModel(tempCellRequestDto)
                val selfLink = linkTo(CellController::class.java).slash(foundCell.cellId).slash("cellRequest").slash(it.cellRequestId).withSelfRel()
                cellModel.add(selfLink)
            }

            val selfLink = linkTo(methodOn(CellController::class.java).getCellRequests(cellId)).withSelfRel()
            val resultEntityModel = CollectionModel(cellsRequestEntityModel, selfLink)

            return ResponseEntity.ok(resultEntityModel)

        }catch (e: Exception){
            println(e.message)
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @PostMapping("/{cellId}/cellRequests/accounts/{accountId}")
    fun createCellRequests(@PathVariable cellId: Long,
                           @PathVariable accountId: Long): ResponseEntity<Any>{

        try {
            // s: validator
            var errorList = ArrayList<ErrorVo>()
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "Not exits this account.", errorList = errorList)
            }
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "Not exits this cell.", errorList = errorList)
            }
            val foundCellRequest = cellRequestService.findCellRequestsWithCellIdAndAccountId(cellId = cellId, accountId = accountId)
            if (foundCellRequest != null) {
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "This account already required this cell.", errorList = errorList)
            }
            val foundJoinedCell = cellService.findAccountInCell(cellId, accountId)
            if (foundJoinedCell != null) {
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "This account already joined this cell.", errorList = errorList)
            }
            if(errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            val cell = cellService.getCellWithId(cellId = cellId)
            val requestAccount = accountService.getAccountById(accountId)
            val cellRequest = CellRequest(cell = cell!!, accountId = requestAccount?.accountId, accountName = requestAccount?.accountname)

            cellRequestService.createCellRequest(cellRequest)

            val entityModel = cellRequest.run {
                val cellRequesDto = CellRequestDto(cellRequestId = cellRequest.cellRequestId, cellId = cell.cellId, accountId = accountId, createDate = cellRequest.createDate)
                val cellRequestModel = CellRequestEntityModel(cellRequesDto)
                val selfLink = linkTo(CellController::class.java).slash(cellId).slash("cellRequests").slash(this.accountId).withSelfRel()
                cellRequestModel.add(selfLink)
            }

            val linkBuilder = linkTo(methodOn(CellController::class.java).createCellRequests(cellId, accountId)).withSelfRel()
            val createdUri = linkBuilder.toUri()

            return ResponseEntity.created(createdUri).body(entityModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

    @DeleteMapping("/{cellId}/cellRequests/accounts/{accountId}")
    fun deleteCellRequests(@PathVariable cellId: Long,
                           @PathVariable accountId: Long): ResponseEntity<Any>{

        try {
            // s: validator
            var errorList = ArrayList<ErrorVo>()
            val foundAccount = accountService.getAccountById(accountId)
            if(foundAccount == null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "Not exits this account.", errorList = errorList)
            }
            val foundCell = cellService.getCellWithId(cellId)
            if(foundCell == null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "Not exits this cell.", errorList = errorList)
            }
            val foundCellRequest = cellRequestService.findCellRequestsWithCellIdAndAccountId(cellId = cellId, accountId = accountId)
            if (foundCellRequest == null) {
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "Not exits cell request of this information.", errorList = errorList)
            }
            if(errorList.isNotEmpty()) {
                val body = errorHelper.getErrorAttributes(errorList)
                return ResponseEntity.badRequest().body(body)
            }
            // e: validator

            if(cellRequestService.deleteCellRequestsWithCellIdAndAccountId(cellId, accountId) != 1L){
                val body = errorHelper.getUnexpectError("Cannot delete cell request.")
                return ResponseEntity.badRequest().body(body)
            }

            val tempCellRequestDto = CellRequestDto(cellRequestId = foundCellRequest!!.cellRequestId, cellId = cellId, accountId = foundCellRequest.accountId, accountName = foundCellRequest.accountName!!, createDate = foundCellRequest.createDate)
            val cellModel = CellRequestEntityModel(tempCellRequestDto)
            val selfLink = linkTo(methodOn(CellController::class.java).deleteCellRequests(cellId, accountId)).withSelfRel()
            cellModel.add(selfLink)

            return ResponseEntity.ok(cellModel)
        }catch (e: Exception){
            val body = errorHelper.getUnexpectError("Please try again..")
            return ResponseEntity.badRequest().body(body)
        }
    }

}
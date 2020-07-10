package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.service.CellRequestService
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.CellRequest
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import me.ewan.cellit.domain.cell.vo.dto.CellRequestDto
import me.ewan.cellit.domain.cell.vo.dto.validator.CellDtoValidator
import me.ewan.cellit.domain.cell.vo.entityModel.CellRequestEntityModel
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import me.ewan.cellit.domain.channel.api.ChannelController
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import me.ewan.cellit.global.common.ConvertQueryToClass
import me.ewan.cellit.global.error.ErrorHelper
import me.ewan.cellit.global.error.ErrorToJson
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
    private lateinit var errorToJson: ErrorToJson

    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var errorHelper: ErrorHelper

    @GetMapping
    fun getCells(@RequestParam query: String?): ResponseEntity<Any>{

        if(query == null){
            // TODO
            println("11111")
            return ResponseEntity.badRequest().body("aaa")
        }else {
            try{
                val convertedQuery = ConvertQueryToClass.convert<CellQuery>(query)
                val cells = cellService.getCellsWithQuery(convertedQuery)

                val cellsEntityModel = cells.map {
                    val tempCellDto = modelMapper.map(it, CellDto::class.java)
                    val cellModel = CellEntityModel(tempCellDto, "")
                    val selfLink = linkTo(CellController::class.java).slash(it.cellId).withSelfRel()
                    cellModel.add(selfLink)
                }

                val selfLink = linkTo(CellController::class.java).withSelfRel()
                val resultEntityModel = CollectionModel(cellsEntityModel, selfLink)

                return ResponseEntity.ok(resultEntityModel)

            }catch (e: Exception){
                println("2")
                println(e.message)
                return ResponseEntity.badRequest().body(e.message)
            }
        }
    }

    @PostMapping
    fun createCell(@RequestBody @Valid cellDto: CellDto): ResponseEntity<Any> {

        // s: validator
        val errorList = cellDtoValidator.validate(cellDto)
        if(errorList.isNotEmpty()){
            val body = errorHelper.getErrorAttributes(errorList)
            return ResponseEntity.badRequest().body(body)
        }
        // e: validator

        val auth = SecurityContextHolder.getContext().authentication

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
    }

    @GetMapping("/{cellId}/channels")
    fun getChannelsWithCellId(@PathVariable cellId: Long): ResponseEntity<CollectionModel<ChannelEntityModel>>{
        val channels = channelService.getChannelDtosWithCellId(cellId)

        val channelsEntityModel = channels.map{
            val channelModel = ChannelEntityModel(modelMapper.map(it, ChannelDto::class.java))
            val selfLink = linkTo(ChannelController::class.java).slash(it.channelId).withSelfRel()
            channelModel.add(selfLink)
        }

        val selfLink = linkTo(methodOn(CellController::class.java).getChannelsWithCellId(cellId)).withSelfRel()
        val resultEntityModel = CollectionModel(channelsEntityModel, selfLink)

        return ResponseEntity.ok(resultEntityModel)
    }

    @PostMapping("/{cellId}/cellRequests/accounts/{accountId}")
    fun createCellRequests(@PathVariable cellId: Long,
                           @PathVariable accountId: Long): ResponseEntity<Any>{

        // s: validator
        try{
            var errorList = ArrayList<ErrorVo>()
            val foundCellRequest = cellRequestService.findCellRequestsWithCellIdAndAccountId(cellId = cellId, accountId = accountId)
            if(foundCellRequest != null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "This account already required this cell.", errorList = errorList)
            }
            val foundJoinedCell = cellService.findAccountInCell(cellId, accountId)
            if(foundJoinedCell != null){
                errorList = errorHelper.addErrorAttributes(status = BAD_REQUEST, message = "This account already joined this cell.",  errorList = errorList)
            }
            val body = errorHelper.getErrorAttributes(errorList)
            return ResponseEntity.badRequest().body(body)
        }catch (e: Exception){
            println(e.message)
        }
        // e: validator

        val cell = cellService.getCellWithId(cellId = cellId)
        val requestAccount = accountService.getAccountWithId(accountId)
        val cellRequest = CellRequest(cell = cell, accountId = accountId)

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
    }
}
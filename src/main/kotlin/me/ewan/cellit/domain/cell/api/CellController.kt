package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.entityModel.CellEntityModel
import me.ewan.cellit.domain.cell.service.CellService
import me.ewan.cellit.domain.cell.vo.domain.Cell
import me.ewan.cellit.domain.cell.vo.domain.CellRole
import me.ewan.cellit.domain.cell.vo.dto.validator.CellDtoValidator
import me.ewan.cellit.domain.cell.vo.query.CellQuery
import me.ewan.cellit.domain.channel.api.ChannelController
import me.ewan.cellit.domain.channel.service.ChannelService
import me.ewan.cellit.domain.channel.vo.dto.ChannelDto
import me.ewan.cellit.domain.channel.vo.entityModel.ChannelEntityModel
import me.ewan.cellit.global.common.ConvertQueryToClass
import me.ewan.cellit.global.common.ErrorToJson
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.Errors
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
    private lateinit var channelService: ChannelService

    @Autowired
    private lateinit var cellDtoValidator: CellDtoValidator

    @Autowired
    private lateinit var errorToJson: ErrorToJson

    @Autowired
    lateinit var modelMapper: ModelMapper

    @GetMapping
    fun getCells(@RequestParam query: String?): ResponseEntity<Any>{

        if(query == null){
            // TODO
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
                return ResponseEntity.badRequest().body(e.message)
            }
        }
    }

    @PostMapping
    fun createCell(@RequestBody @Valid cellDto: CellDto, errors: Errors): ResponseEntity<Any> {

        cellDtoValidator.validate(cellDto, errors)
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errorToJson.convert(errors))
        }

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
}
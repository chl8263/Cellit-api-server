package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.vo.dto.CellDto
import me.ewan.cellit.domain.cell.vo.model.CellModel
import me.ewan.cellit.domain.cell.service.CellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/cells"], produces = [MediaTypes.HAL_JSON_VALUE])
class CellController {

    @Autowired
    private lateinit var cellService: CellService

    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping
    fun createCell(@RequestBody cellDto: CellDto): ResponseEntity<CellModel> {

        val auth = SecurityContextHolder.getContext().authentication

        val savedCell = cellService.createCell(cellDto, auth.name)

        val entityModel = savedCell.let {
            val cellModel = CellModel(it)
            val selfLink = linkTo(CellController::class.java).slash(it.cellId)
                    .withSelfRel()
            cellModel.add(selfLink)
        }

        val linkBuilder = linkTo(CellController::class.java).slash(savedCell.cellId)
        val createdUri = linkBuilder.toUri()

        return ResponseEntity.created(createdUri).body(entityModel)
    }

//    @PostMapping("/test")
//    fun testCellPost(model: Model): Map<String, String>{
//
//        return mapOf("test" to "testSuccess")
//    }
}
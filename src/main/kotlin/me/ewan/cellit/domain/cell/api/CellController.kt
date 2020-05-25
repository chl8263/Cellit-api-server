package me.ewan.cellit.domain.cell.api

import me.ewan.cellit.domain.account.service.AccountService
import me.ewan.cellit.domain.cell.model.CellDto
import me.ewan.cellit.domain.cell.model.CellModel
import me.ewan.cellit.domain.cell.service.CellService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping(value = ["/api/cells"], produces = [MediaTypes.HAL_JSON_VALUE])
class CellController {

    @Autowired
    private lateinit var cellService: CellService

    @Autowired
    private lateinit var accountService: AccountService

//    @GetMapping("/{accountId}")
//    fun getCellsFromAccountId(@PathVariable accountId: Long): ResponseEntity<CellModel>{
//
//        val cells = accountService.getCellsWithAccountId(accountId)
//
//        val cellModel = CellModel(cells[0])
//
//        return ResponseEntity.ok(cellModel)
//    }

//    val cells = accountService.getCellsWithAccountId(accountId)
//
//    val entityModel = cells.map {
//        val cellModel = CellModel(it)
//        val selfLink = linkTo(CellController::class.java).slash(it.cellId)
//                .withSelfRel()
//        cellModel.add(selfLink)
//    }
//
//    val selfLink = linkTo(WebMvcLinkBuilder.methodOn(AccountController::class.java).getCellsFromAccountId(accountId)).withSelfRel()
//
//    val resultEntityModel = CollectionModel(entityModel, selfLink)
//
//    return ResponseEntity.ok(resultEntityModel)

    @PostMapping
    fun createCell(@RequestBody cellDto: CellDto, principal: Principal): ResponseEntity<CellModel> {

        val savedCell = cellService.createCell(cellDto, principal.name)

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